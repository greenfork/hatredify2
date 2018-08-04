(ns hatredify2.main
  "Main IO logic of the program. Web-server management, routing and middleware."
  (:require [org.httpkit.server :as s]
            [environ.core :refer [env]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.util.response :as res]
            [hatredify2.config :as cfg]
            [hatredify2.template :as t]
            [hatredify2.core :as hc]
            [hatredify2.wordnet :as wd]
            [bidi.ring :refer [make-handler]])
  (:gen-class))

(defn index-handler [request]
  (-> {:pure-chunk "What a wonderful weather outside!"
       :request-method :get}
      t/index
      res/response))

(defn post-handler [request]
  (let [pure-chunk ((:params request) "pure-chunk")]
    (-> {:pure-chunk pure-chunk
         :request-method :post
         :hatredified-chunk (t/highlight (hc/hatredify-text wd/dictionary
                                                            pure-chunk))}
        t/index
        res/response)))

(def routes ["/" {:get index-handler
                  :post post-handler}])

(def handler
  (make-handler routes))

(def app (-> handler
             cfg/special-middleware
             wrap-params
             wrap-file-info
             (wrap-file "resources/public")))

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
  (println (str "Starting web-server on localhost:" (env :port)))
  (start-server))
