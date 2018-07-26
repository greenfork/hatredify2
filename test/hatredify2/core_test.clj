(ns hatredify2.core-test
  (:require [clojure.test :refer :all]
            [hatredify2.core :refer :all]))

(defn ci
  "Concatenate `identifier`."
  [word]
  (str word identifier))

(deftest unit-main-logic
  (let [s1 "It is a good day, sir!"
        coll1 ["It" " " "is" " " "a" " " "good" " " "day" ", " "sir" "!"]
        rcoll1 ["It" " " "is" " " "a" " " (ci "AWFUL") " " "day" ", " "sir" "!"]
        rs1 (str "It is a " (ci "AWFUL") " day, sir!")
        rs1-articles (str "It is an " (ci "AWFUL") " day, sir!")
        s2 "It's so warm outside!"
        coll2 ["It" "'" "s" " " "so" " " "warm" " " "outside" "!"]
        rcoll2 ["It" "'" "s" " " "so" " " (ci "COLD") " " "outside" "!"]
        rs2 (str "It's so " (ci "COLD") " outside!")
        antonyms {"good" #{"awful"} "warm" #{"cold"}}]
    (testing "splits s to tokens"
      (is (= (split-to-tokens s1) coll1))
      (is (= (split-to-tokens s2) coll2)))
    (testing "replaces words with antonyms"
      (let [f (partial replace-with-antonyms antonyms)]
        (is (= (f coll1) rcoll1))
        (is (= (f coll2) rcoll2))))
    (testing "changes to proper articles"
      (is (=  (change-articles rs1) rs1-articles)))
    (testing "whole pipeline"
      (is (= (hatredify-text antonyms s1) rs1-articles))
      (is (= (hatredify-text antonyms s2) rs2)))))
