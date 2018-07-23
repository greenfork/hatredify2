(defproject hatredify2 "0.1.0-SNAPSHOT"
  :description "Web-application which changes chunks of text to more evil form."
  :url "https://github.com/greenfork/hatredify2"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :main ^:skip-aot hatredify2.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
