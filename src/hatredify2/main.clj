(ns hatredify2.main
  (:require [org.httpkit.server :as s]
            [environ.core :refer [env]]
            [hatredify2.config :as cfg])
  (:gen-class))

(defn handler [request]
  {:status 200
   :headers {"Content Type" "text/html"}
   :body "HATE! MORE HATE! MORE YASS HATE!"})

(def app (cfg/special-middleware handler))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)))

(defn -main
  "Launch the web-server."
  [& args]
  (reset! server (s/run-server #'app
                               {:port (Integer/parseInt (env :port))})))
