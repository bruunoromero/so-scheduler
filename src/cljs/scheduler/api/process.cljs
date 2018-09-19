(ns scheduler.api.process
  (:require [cljs-time.core :as time]))

(defonce id-seq (atom 0))

(defrecord Process [id time elapsed status priority deadline interval aborted?]) 

(defn generate-interval! []
  (time/plus (time/now) (time/seconds (rand-int 20))))

(defn rand-range! [from to]
  (let [r (rand-int to)]
    (if (< r from)
      (recur from to)
      r)))

(defn create-process! []
  (Process. 
    (swap! id-seq inc)
    (rand-range! 4 20)
    0
    :awaiting
    (rand-int 3)
    (rand-range! 4 20)
    (generate-interval!)
    false))


(defn inc-elapsed [process]
  (update-in process [:elapsed] inc))
