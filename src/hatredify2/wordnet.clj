(ns hatredify2.wordnet
  (:require [clj-wordnet.core :as wd]
            [environ.core :refer [env]]))

(def wordnet (wd/make-dictionary (env :wordnet-directory)))
(def positive-words (line-seq (clojure.java.io/reader (env :positive-words))))

(defn- find-antonyms
  "Return a list of wordnet records of antonyms related to the given `word-id`."
  [word-id]
  ((comp :antonym wd/lexical-relations wordnet) word-id))

(defn- find-similar-words
  "Return a list of wordnet records of similar adjectives to the given
  `word-id`."
  [word-id]
  (let [similar-synsets
        ((comp :similar-to wd/semantic-relations wd/synset wordnet) word-id)]
    (filter #(= :adjective (:pos %))
            (mapcat wd/words similar-synsets))))

(defn- extract-ids
  "Return a list of wordnet word IDs from a given `word`.
  In wordnet one word can contain different amount of meanings, each has its
  own ID."
  [word]
  (map :id (wordnet word :adjective)))

(defn- extract-antonyms
  "Return a set of adjectives, antonymous to the given `word`."
  [word]
  (let [word-ids (extract-ids word)
        similar-words-ids
        (map :id (filter some? (mapcat find-similar-words word-ids)))]
    (->> (concat word-ids similar-words-ids)
         (keep find-antonyms)
         (flatten)
         (map :lemma)
         (into #{}))))

(defn- extract-similar-words
  "Return a set of adjectives, which are similar to the `word`."
  [word]
  (->> (extract-ids word)
       (mapcat find-similar-words)
       (map :lemma)
       (into #{})))

(defn- extract-synonyms
  "Return a set of adjectives with at least 1 synset in common with `word`.
  `word` is returned as well as it has its synset in common."
  [word]
  (->> (wordnet word :adjective)
       (map wd/synset)
       (mapcat wd/words)
       (map :lemma)
       (into #{})))

(defn- form-dictionary-map
  "Return a dictionary which maps words to the set of their antonyms."
  [word-list]
  (->> word-list
       (reduce #(clojure.set/union %1 (extract-similar-words %2)) #{})
       (clojure.set/union (set word-list))
       (reduce #(clojure.set/union %1 (extract-synonyms %2)) #{})
       (map #(list % (extract-antonyms %)))
       (filter (comp not empty? second))
       flatten
       (apply hash-map)))

(def dictionary (form-dictionary-map positive-words))
