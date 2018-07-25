(ns hatredify2.main
  (:require [org.httpkit.server :as s]
            [environ.core :refer [env]]
            [ring.middleware.params :refer [wrap-params]]
            [hatredify2.config :as cfg]
            [hatredify2.template :as t])
  (:gen-class))

(defn handler [request]
  {:status 200
   :headers {"Content Type" "text/html"}
   :body (t/index {:pure-chunk "What a wonderful weather outside!"
                   :hatredified-chunk "mememe"
                   :request-method (:request-method request)})})

(def app (-> handler
             cfg/special-middleware
             wrap-params))

(defonce server (atom nil))

(defn start-server []
  (reset! server (s/run-server #'app
                               {:port (Integer/parseInt (env :port))})))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)))

(defn restart-server []
  (stop-server)
  (start-server))

(defn check-port []
  (when-not (env :port)
    (println "Please, specify the PORT variable, e.g. `export PORT=3000`")
    (System/exit 1)))

(defn -main
  "Launch the web-server."
  [& args]
  (check-port)
  (start-server))
