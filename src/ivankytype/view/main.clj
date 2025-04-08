(ns ivankytype.view.main
  (:require [hiccup2.core :refer [html]]
            [ring.util.response :refer [response]]
            [clojure.java.io :as io]
            [ivankytype.text-gen :as gen]))


(defn- index-page [size]
  (html [:html
         [:head
          [:title "Ivankytype"]
          [:link {:href "css/style.css"
                  :rel "stylesheet"
                  :type "text/css"}]]
         [:body
          [:script {:src "js/compiled/ivankytype.js"
                    :type "text/javascript"}]
          [:p (gen/get-text size)]]]))


(defn index [size]
  (-> (index-page size)
      (str)
      (response)
      (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))


(comment
  (index 100)
  (str (index-page))
  (slurp (io/resource "style.css")))
