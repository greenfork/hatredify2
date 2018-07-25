(ns hatredify2.template
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate index "templates/index.html"
  [{:keys [pure-chunk hatredified-chunk request-method]}]
  [:head :title] (html/content "Hatredify")
  [:textarea#pure-chunk] (html/content pure-chunk)
  [:textarea#hatredified-chunk] (when (= request-method :post)
                                  (html/content hatredified-chunk)))
