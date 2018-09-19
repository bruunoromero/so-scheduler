(ns scheduler.api.sjf
  (:require [scheduler.api.process :as p]))

(defn create-processes! []
  (->> (range (rand-int 10))
    (map p/create-process!)
    (vec)))

(defn push-process [processes process]
  (-> processes
    (conj process)))

(defn scheduler [state]
  (let [ables (:ables state)
        running (:running state)
        up-running (map #(p/inc-elapsed %) running)
        finished (filter #(= (:elapsed %) (:time %)) up-running)
        still-running (filter #(not= (:elapsed %) (:time %)) up-running)
        free-cores (- (:cores state) (count still-running))
        new-running (concat still-running (take free-cores ables))
        still-ables (drop free-cores ables)
        staggered (concat (:staggered state) finished)]
    (assoc state :ables (vec still-ables) :running (vec new-running) :staggered (vec staggered))))
