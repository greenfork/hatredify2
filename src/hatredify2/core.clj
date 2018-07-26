(ns hatredify2.core
  (:require [clojure.string :as s]))

(def identifier "hjl")

(defn split-to-tokens
  "Split `text` to the collection of tokens, not necessarily words."
  [text]
  (s/split text #"\b"))

(defn replace-with-antonyms
  "Replace each word in `tokens` with antonym from `dict`, if present.
  Additionally upper-case them. `dict` maps each word to the set of its
  antonyms. At the end of each replaced word an `identifier` is appended
  for future processing."
  [dict tokens]
  (map #(if-let [antonyms (dict (s/lower-case %))]
          ((comp (fn [s] (str s identifier)) s/upper-case rand-nth seq) antonyms)
          %)
       tokens))

(defn change-articles
  "Changes 'a' article to 'an' and in reverse where necessary."
  [text]
  (-> text
      (s/replace #"([aA]) ([AEIOU])" "$1n $2")
      (s/replace #"([aA])n ([QWRTYPSDFGHJKLZXCVBNM])" "$1 $2")))

(defn hatredify-text
  "Finds all positive adjectives, replaces with antonyms, makes uppercase."
  [dict text]
  (->> text
       (split-to-tokens)
       (replace-with-antonyms dict)
       (apply str)
       (change-articles)))
