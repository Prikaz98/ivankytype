(ns ivankytype-ui.core
  (:require [clojure.string :refer [blank?]]))

;;TODO: Store metrics of all clicks and draw graph
;;TODO: Add animation when restart

(def input (.getElementById js/document "input"))
(def txt (.getElementById js/document "txt"))
(def done (.getElementById js/document "done"))
(def stat (.getElementById js/document "stat"))
(def sizes (.getElementById js/document "sizes"))
(def warn (.getElementById js/document "warn"))
(def restart (.getElementById js/document "restart"))

(def start-time (atom nil))
(def finished-time (atom nil))
(def attempts (atom 0))

(defn floor [num scale]
  (let [multip (js/Math.pow 10 scale)]
    (/ (js/Math.round (* num multip)) multip)))

(defn fill-stat []
  (let [seconds (if @start-time (floor (/ (- (or @finished-time (js/Date.now)) @start-time) 1000) 2) 0)]
    (set!
     (.-innerText stat)
     (str seconds " seconds" " / " @attempts " attempts"))))

(defn input-listener-keydown [event]
  (when (nil? @start-time)
    (reset! start-time (js/Date.now)))

  (let [key (.-key event)]
    (when (= (count key) 1)
      (let [content (.-innerText txt)
            head (nth content 0)]
        (if (= head key)
          (do
            (set! (.-innerText done) (str (.-innerText done) head))
            (when (blank? (set! (.-innerText txt) (.substring content 1)))
              (reset! finished-time (js/Date.now))))
            (do
              (swap! attempts #(+ 1 %))
              (set! (.-innerText txt) (str (.-innerText done) (.-innerText txt)))
              (set! (.-innerText done) "")))))))

(defn input-listener-focusout [event]
  (set! (.-innerText warn) "Click on text to continue"))

(defn txt-listener-click [event]
  (set! (.-innerText warn) "")
  (.focus input))

(defn restart-listener-click [event]
  (.reload js/location))

(.addEventListener input "keydown" input-listener-keydown)
(.addEventListener input "focusout" input-listener-focusout)
(.addEventListener txt "click" txt-listener-click)
(.addEventListener restart "click" restart-listener-click)

(fill-stat)
(js/setInterval fill-stat 100)

(doseq [button (.-children sizes)]
  (let [value (.-innerText button)]
    (.addEventListener
     button
     "click"
     (fn [_]
       (.replace js/location (str (.-url js/document) "?size=" value))))))
