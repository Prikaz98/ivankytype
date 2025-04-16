(ns ivankytype.view.main
  (:require [hiccup2.core :refer [html]]
            [ring.util.response :refer [response]]
            [clojure.java.io :as io]
            [ivankytype.text-gen :as gen]))


(defn- index-page [size]
  (html [:html

         [:head
          [:title "Ivankytype"]
          [:link {:href "https://pvinis.github.io/iosevka-webfont/3.4.1/iosevka.css"
                  :rel "stylesheet"}]
          [:link {:href "css/style.css"
                  :rel "stylesheet"
                  :type "text/css"}]]
         [:body
          [:div {:class "settings"}
           [:div {:id "sizes"}
            [:button (when (and size (= size 10)) {:class "active"}) "10"]
            [:button (when (and size (= size 25)) {:class "active"}) "25"]
            [:button (when (and size (= size 50)) {:class "active"}) "50"]
            [:button (when (and size (= size 100)) {:class "active"}) "100"]]]
          [:div {:class "info"}
           [:div {:style "float: right;"}
            [:label {:id "stat"}]]
           [:p
            [:a {:href "https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki"}
             "BIP-39"]
            " Typing trainer"]]
          [:div {:class "tofill"}
           [:p
            [:label {:id "done"} ""]
            [:label {:id "txt"
                     :class "unfill"}
             (gen/get-text size)]]]

          [:label {:id "warn"
                   :class "warn"}]

          [:button {:id "restart"
                    :class "restart-btn"}
           [:img {:src "images/restart.png"}]]

          [:textarea {:id "input"
                        :autofocus true
                        :class "hide"}]
          [:script {:src "js/compiled/ivankytype.js"
                    :type "text/javascript"}]]]))


(defn index [size]
  (-> (index-page size)
      (str)
      (response)
      (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))

(comment
  (index 100)
  (str (index-page))
  (slurp (io/resource "style.css")))
