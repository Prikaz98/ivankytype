(ns ivankytype.text-gen
  (:require [clojure.java.io :as io]
            [clojure.math :refer [random]]
            [clojure.string :refer [join]]))

;;TODO: handle case when resource is not loaded
(def ^{:private true} bip39 (vec (.split (slurp (io/resource "public/bip39")) "\n")))
(def ^{:private true} count-threshold 20)

(defn get-text [size]
  (loop [acc nil]
    (if (< (count acc) (or size count-threshold))
      (recur (conj acc
                   (bip39 (.intValue (* (+ 1 (count bip39)) (random))))))
      (join " " acc))))

(comment
  (get-text)
  )
