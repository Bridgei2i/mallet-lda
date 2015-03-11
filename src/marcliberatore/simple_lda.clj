(ns marcliberatore.simple-lda
  (:require [clojure.edn :as edn]
            [incanter.core :refer [matrix]]
      [marcliberatore.mallet-lda :refer [make-instance-list lda]])
  (:import [cc.mallet.types Alphabet LabelAlphabet FeatureSequence Instance InstanceList])
  (:import [cc.mallet.util Randoms])
  (:import cc.mallet.topics.SimpleLDA))

(def dpath "/media/kiran/5A76869F76867B8F/datasets/AppleData/apple-data.txt")

(defn read-dataset
  [inpfile]
  (with-open [r (clojure.java.io/reader inpfile)]
    (mapv edn/read-string (line-seq r))))

(defn create-instances
  "create an instance-list object "
  [dataset]
  (make-instance-list (mapv vector (iterate inc 1) dataset)))
(def ilist (create-instances (read-dataset dpath)))

(def BETA 0.01)

(defn newLabelAlphabet
  "TODO: replace with (Alphabet. capacity)"
  [num-topics]
  (let [la (LabelAlphabet.)]
    (dotimes [i num-topics]
      (.lookupIndex la (str "topic" i)))
    la))

(.size (newLabelAlphabet 10))

(defn slda
  ;constructor equivalent
  ([num-topics] (slda num-topics num-topics BETA))
  ([num-topics alphaSum beta] (slda (newLabelAlphabet num-topics) alphaSum beta (Randoms.)))
  ;([num-topics alphaSum beta random] (slda (newLabelAlphabet num-topics) alphaSum beta (Randoms.)))
  ([^LabelAlphabet topic-alphabet alphaSum beta random]
   (let [num-topics (.size topic-alphabet)]
    {:data nil :topic-alphabet topic-alphabet
     :num-topics num-topics
     :alphaSum alphaSum :beta beta :onedoc-topic-counts (matrix 0 num-topics 1)
     :tokens-per-topic (matrix 0 num-topics 1) :random random})))

 (slda 10)

