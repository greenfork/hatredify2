(ns hatredify2.template
  (:require [net.cgrand.enlive-html :as html]
            [hatredify2.core :refer [identifier]]))

(html/deftemplate index "templates/index.html"
  [{:keys [pure-chunk hatredified-chunk request-method]}]
  [:head :title] (html/content "Hatredify")
  [:#pure-chunk] (html/content pure-chunk)
  [:#hatredified-chunk] (if (= request-method :post)
                          (html/html-content hatredified-chunk)
                          identity)
  [:#hatred] (if (= request-method :post)
               (html/remove-attr :hidden)
               identity)
  [:body] (if (= request-method :post)
            (html/do-> (html/add-class "black")
                       (html/remove-class "white"))
            identity))


(def highlight-token "<span class=\"highlight\">")

(defn html-highlight-word
  "Use html tags to highlight a `word`."
  [word]
  (str highlight-token word "</span>"))

(defn highlight
  "Highlight all words written with capitals and with `identifier` at the end.
  Remove any `identifier`."
  [text]
  (let [re (re-pattern (str "\\b([A-Z]{2,})" identifier "\\b"))]
    (clojure.string/replace text re (html-highlight-word "$1"))))

(defn nl2br
  "Change newlines of `text` to <br> tokens."
  [text]
  (clojure.string/replace text #"\r\n" "<br>"))

(defn htmlize
  "Process `text` to be a nice html piece."
  [text]
  (-> text
      highlight
      nl2br))
