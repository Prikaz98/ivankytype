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
(def words-counter (.getElementById js/document "words-counter"))
(def threshold (- (count (.-innerText txt)) 1))

(def is-easy (= "active" (.-className easy)))
(def restarted (atom false))

(def start-time (atom nil))
(def finished-time (atom nil))
(def attempts (atom 0))

(defn actual-words-count-done []
  (- (count (split (.-innerText done) #" ")) 1))

(defn floor [num scale]
  (let [multip (js/Math.pow 10 scale)]
    (/ (js/Math.round (* num multip)) multip)))


(defn fill-stat []
  (let [seconds (if @start-time (floor (/ (- (or @finished-time (js/Date.now)) @start-time) 1000) 2) 0)]
    (set!
     (.-innerText stat)
     (str
      seconds " seconds"
      (if (not is-easy) (str " / " @attempts " attempts") "")))))


(defn hard-mistake-behavior [to]
  (reset! restarted true)
  (swap! attempts #(+ 1 %))
  (doseq [id (reverse (range 0 to))]
    (let [el (.getElementById js/document id)]
      (.add (.-classList el) "unfill")
      (.prepend txt el))))


(defn easy-mistake-behavior [el]
  (.append done el)
  (set! (.-className el) "error"))


(defn handle-key-press! [head key el index]
  (if (= head key)
    (do
      (.remove (.-classList el) "unfill")
      (.append done el))
    (if is-easy
      (easy-mistake-behavior el)
      (hard-mistake-behavior index))))


(defn handle-backspace! [index]
  (let [prev-el (.getElementById js/document (- index 1))]
    (set! (.-className prev-el) "unfill")
    (.prepend txt prev-el)))


(defn input-listener-keydown [event]
  (when (nil? @start-time)
    (reset! start-time (js/Date.now)))

  ;;dirty hack
  (when @restarted
    (set! (.-value input) "")
    (reset! restarted false))

  (when (nil? @finished-time)
    (let [key (.-key event)
          index (count (.-value input))
          content (.-innerText txt)
          head (first content)
          el (.getElementById js/document index)]

      (cond
        (= (count key) 1) (handle-key-press! head key el index)
        (= key "Backspace") (handle-backspace! index)
        :else (do (js/console.log "Unexpected event") (js/console.log event)))
      (set! (.-innerText words-counter) (actual-words-count-done))
      (when (<= threshold index)
        (reset! finished-time (js/Date.now))
        (set! (.-innerText words-counter) (+ (actual-words-count-done) 1))))))


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
