(ns scheduler.utils)

(defn value [e]
  (.-value (.-target e)))