(ns scheduler.api.process
  (:require [cljs-time.core :as time]
            [cljs-time.coerce :as time-coerce]))

(defonce id-seq (atom 0))

(defrecord Process [id time elapsed status priority deadline interval aborted? created-at]) 

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
    (generate-interval!)
    false))

(defn inc-elapsed [process]
  (update-in process [:elapsed] inc))

(defn expired? [process]
  (time/equal? (time/now) (:deadline process)))

(defn sort-deadline [processes]
  (sort-by #(time-coerce/to-long (:deadline %)) processes))
