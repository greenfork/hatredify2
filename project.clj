(defproject hatredify2 "0.1.0-SNAPSHOT"
  :description "Web-application which changes chunks of text to more evil form."
  :url "https://github.com/greenfork/hatredify2"
  :license {:name "MIT"
            :url "https://github.com/greenfork/hatredify2/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.7.0-RC1"]
                 [http-kit "2.3.0"]
                 [environ "1.1.0"]]
  :main ^:skip-aot hatredify2.main
  :target-path "target/%s"
  :plugins [[lein-environ "1.1.0"]]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[ring/ring-devel "1.7.0-RC1"]]
                   :env {:port 3000}}})
