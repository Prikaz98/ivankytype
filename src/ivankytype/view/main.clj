(ns ivankytype.view.main
  (:require [hiccup2.core :refer [html]]
            [ring.util.response :refer [response]]
            [clojure.java.io :as io]
            [ivankytype.text-gen :as gen]))

(defn- settings-block [size mode]
  [:div {:class "settings"}
   [:div {:id "sizes"}
    [:button (when-not (and size (not (= size 10))) {:class "active"}) "10"]
    [:button (when (and size (= size 25)) {:class "active"}) "25"]
    [:button (when (and size (= size 50)) {:class "active"}) "50"]
    [:button (when (and size (= size 100)) {:class "active"}) "100"]]

   [:div
    [:label {:style "margin-left: 10px"} "|"]
    [:button {:id "easy-mode"
              :title "Allows to do mistakes"
              :class (if-not (and mode (not (= mode "easy"))) "active" "none")}
     "easy"]
    [:button {:id "hard-mode"
              :title "Mistake restarts level"
              :class (if (and mode (= mode "hard")) "active" "none")}
     "hard"]]])


(defn- info-block []
  [:div {:class "info"}
   [:div {:style "float: right;"}
    [:label {:id "stat"}]]
   [:p
    [:a {:href "https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki"}
     "BIP-39"]
    " Typing trainer"]])


(defn- text-block [size]
  [:div {:class "tofill"}
   [:p
    [:label {:id "done"} ""]
    [:label {:id "txt"}
     (map-indexed
      (fn [i c] [:label {:id i :class "unfill"} c])
      (vec (gen/get-text size)))]]])


(defn- footer-block []
  [:div
   [:label {:id "warn"
            :class "warn"}]
   [:button {:id "restart"
             :class "restart-btn"}
    [:img {:src "images/restart.png"}]]])


(defn- index-page [size mode]
  (html [:html
         [:head
          [:title "Ivankytype"]
          [:link {:href "https://pvinis.github.io/iosevka-webfont/3.4.1/iosevka.css"
                  :rel "stylesheet"}]
          [:link {:href "css/style.css"
                  :rel "stylesheet"
                  :type "text/css"}]]
         [:body
          (settings-block size mode)
          (info-block)
          (text-block size)
          (footer-block)

          [:textarea {:id "input"
                        :autofocus true
                        :class "hide"}]
          [:script {:src "js/compiled/ivankytype.js"
                    :type "text/javascript"}]]]))


(defn index [size mode]
  (-> (index-page size mode)
      (str)
      (response)
      (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))


(comment
  (index 100)
  (str (index-page))
  (slurp (io/resource "style.css"))
  (text-block 10))