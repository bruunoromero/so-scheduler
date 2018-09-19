(ns scheduler.api.ltg
  (:require [scheduler.api.process :as p]))
  
(defn create-processes! []
  (->> (range (rand-int 10))
    (map p/create-process!)
    (p/sort-deadline)
    (vec)))

(defn push-process [processes process]
  (-> processes
    (conj process)
    (p/sort-deadline)))

(defn scheduler [state]
  (let [ables (:ables state)
        running (:running state)
        expired (->> ables 
                  (filter #(p/expired? %))
                  (map #(assoc % :aborted? true)))
        up-running (map #(p/inc-elapsed %) running)
        finished (filter #(= (:elapsed %) (:time %)) up-running)
        still-running (filter #(not= (:elapsed %) (:time %)) up-running)
        free-cores (- (:cores state) (count still-running))
        new-running (concat still-running (take free-cores ables))
        still-ables (drop free-cores ables)
        staggered (concat (:staggered state) finished expired)]
    (assoc state :ables (vec still-ables) :running (vec new-running) :staggered (vec staggered))))
