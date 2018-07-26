(defproject hatredify2 "0.1.0-SNAPSHOT"
  :description "Web-application which changes chunks of text to more evil form."
  :url "https://github.com/greenfork/hatredify2"
  :license {:name "MIT"
            :url "https://github.com/greenfork/hatredify2/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.7.0-RC1"]
                 [http-kit "2.3.0"]
                 [environ "1.1.0"]
                 [enlive "1.1.6"]
                 [clj-wordnet "0.3.0"]]
  ;; Includes custom build of clj-wordnet library
  :repositories [["local"
                  ~(str (.toURI (java.io.File. "resources/libs")))]]

  :main ^:skip-aot hatredify2.main
  :target-path "target/%s"
  :uberjar-name "hatredify2.jar"
  :plugins [[lein-environ "1.1.0"]]
  :profiles {:uberjar {:aot :all
                       :source-paths ["profiles/prod"]}
             :dev {:dependencies [[ring/ring-devel "1.7.0-RC1"]]
                   :source-paths ["profiles/dev"]
                   :env {:port "3000"
                         :wordnet-directory "resources/wordnet/dict_en"
                         :positive-words "resources/wordnet/positive_words.txt"}}})
