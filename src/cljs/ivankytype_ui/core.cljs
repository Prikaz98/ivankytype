(ns ivankytype-ui.core
  (:require [clojure.string :refer [blank? split]]))

;;TODO: Store metrics of all clicks and draw graph
;;TODO: Add animation when restart

(def input (.getElementById js/document "input"))
(def txt (.getElementById js/document "txt"))
(def done (.getElementById js/document "done"))
(def stat (.getElementById js/document "stat"))
(def sizes (.getElementById js/document "sizes"))
(def warn (.getElementById js/document "warn"))
(def restart (.getElementById js/document "restart"))
(def hard (.getElementById js/document "hard-mode"))
(def easy (.getElementById js/document "easy-mode"))

(def is-easy (= "active" (.-className easy)))

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

(defn hard-mistake-behavior []
  (swap! attempts #(+ 1 %))
  (set! (.-innerText txt) (str (.-innerText done) (.-innerText txt)))
  (set! (.-innerText done) ""))

(defn easy-mistake-behavior []
  ;; TODO: put current char in style error
  )

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
            (if is-easy (easy-mistake-behavior) (hard-mistake-behavior)))))))

(defn input-listener-focusout [_]
  (set! (.-innerText warn) "Click on text to continue"))

(defn txt-listener-click [_]
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

(defn change-search-params [map]
  (let [url (.-URL js/document)
        splitted-url (split url "?")
        clean-url (first splitted-url)
        params (js/URLSearchParams. (or (second splitted-url) ""))]
    (doseq [key (keys map)]
      (.set params (name key) (key map)))
    (str clean-url "?" (.toString params))))

(doseq [button (.-children sizes)]
  (let [value (.-innerText button)]
    (.addEventListener
     button
     "click"
     (fn [_] (.replace js/location (change-search-params {:size value}))))))

(defn modeEventListener [input value]
  (.addEventListener
   input
   "click"
   (fn [_] (.replace js/location (change-search-params {:mode value})))))

(modeEventListener easy "easy")
(modeEventListener hard "hard")
