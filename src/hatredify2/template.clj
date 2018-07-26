(ns hatredify2.template
  (:require [net.cgrand.enlive-html :as html]
            [hatredify2.core :refer [identifier]]))

(html/deftemplate index "templates/index.html"
  [{:keys [pure-chunk hatredified-chunk request-method]}]
  [:head :title] (html/content "Hatredify")
  [:textarea#pure-chunk] (html/content pure-chunk)
  [:textarea#hatredified-chunk] (when (= request-method :post)
                                  (html/content hatredified-chunk)))

(def highlight-token "<span class=\"highlight\">")

(defn html-highlight-word
  "Use html tags to highlight a `word`."
  [word]
  (str highlight-token word "</span"))

(defn highlight
  "Highlight all words written with capitals and with `identifier` at the end.
  Remove any `identifier`."
  [text]
  (let [re (re-pattern (str "\\b([A-Z]{2,})" identifier "\\b"))]
    (clojure.string/replace text re (html-highlight-word "$1"))))
