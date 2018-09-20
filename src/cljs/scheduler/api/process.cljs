(ns scheduler.api.process
  (:require [cljs-time.core :as time]))

(defonce id-seq (atom 0))

(defrecord Process [id time elapsed status priority deadline interval]) 

(defn rand-range! [from to]
  (let [r (rand-int to)]
    (if (< r from)
      (recur from to)
      r)))

(defn generate-interval! []
  (time/plus (time/now) (time/seconds (rand-int 20))))

(defn expiration-time []
  (time/plus (time/now) (time/seconds (rand-range! 4 20))))

(defn create-process! []
  (Process. 
    (swap! id-seq inc)
    (rand-range! 4 20)
    0
    :awaiting
    (rand-int 3)
    (expiration-time)
    (generate-interval!)))
    

(defn inc-elapsed [process]
  (update-in process [:elapsed] inc))

(defn expired? [process]
  (time/before? (:deadline process) (time/now)))

(defn sort-deadline [processes]
  (sort-by #(:deadline %) time/before? processes))
