(ns hatredify2.main
  (:require [org.httpkit.server :as s]
            #_[ring/ring-devel :refer [wrap-reload]])
  (:gen-class))

(defn handler [request]
  {:status 200
   :headers {"Content Type" "text/html"}
   :body "HATE! MORE HATE! EVEN MORE HATE!"})

(def app
  (-> handler
      #_wrap-reload))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)))

(defn -main
  "Launch the web-server."
  [& args]
  (reset! server (s/run-server #'handler {:port 3000})))
