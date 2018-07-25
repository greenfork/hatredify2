(ns hatredify2.config
  (:require [ring.middleware.reload :refer [wrap-reload]]))

(def special-middleware #(wrap-reload %))
