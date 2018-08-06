(ns hatredify2.check
  (:require [environ.core :refer [env]])
  (:import [java.nio.file Files Paths LinkOption]))

(defn env-variables-exist? []
  (doseq [[variable message]
          [[(env :port)
            "Please, specify the PORT variable, e.g. `export PORT=3000`"]
           [(env :wordnet-directory)
            (str "Please, specify the WORDNET_DIRECTORY variable e.g.\n"
                 "`export WORDNET_DIRECTORY=resources/wordnet/dict_en`")]
           [(env :positive-words)
            (str "Please, specify the POSITIVE_WORDS variable e.g. \n`export"
                 " POSITIVE_WORDS=resources/wordnet/positive_words.txt`")]]]
    (when-not variable
      (println message)
      (System/exit 1))))

(defn env-variables-valid? []
  (let [wordnet-directory-string (env :wordnet-directory)
        positive-words-string (env :positive-words)
        string-array (into-array String [])
        linkoption-array (into-array LinkOption [])
        wordnet-directory-path (Paths/get wordnet-directory-string string-array)
        positive-words-path (Paths/get positive-words-string string-array)]
    (when-not (Files/isDirectory wordnet-directory-path linkoption-array)
      (println (str wordnet-directory-string " is not a directory."
                    "\nPlease, change the WORDNET_DIRECTORY variable e.g.\n"
                    "`export WORDNET_DIRECTORY=resources/wordnet/dict_en`"))
      (System/exit 1))
    (when-not (Files/isRegularFile positive-words-path linkoption-array)
      (println (str positive-words-string " is not a regular file."
                    "\nPlease, change the POSITIVE_WORDS variable e.g.\n"
                    "`export POSITIVE_WORDS=resources/wordnet/"
                    "positive_words.txt`"))
      (System/exit 1))))
