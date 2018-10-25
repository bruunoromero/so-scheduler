(ns scheduler.api.ltg
  (:require [scheduler.api.process :as p]))
  
(defn create-processes! []
  (->> (range (rand-int 50))
    (map p/create-process!)
    (vec)
    (p/sort-deadline)))

(defn push-process [processes process]
  (-> processes
    (conj process)
    (p/sort-deadline)))

(defn scheduler [state]
  (let [ables (:ables state)
        running (:running state)
        expired (filter #(p/expired? %) ables)
        not-expired (filter #(not (p/expired? %)) ables)
        up-running (map #(p/inc-elapsed %) running)
        finished (filter #(= (:elapsed %) (:time %)) up-running)
        still-running (filter #(not= (:elapsed %) (:time %)) up-running)
        free-cores (- (:cores state) (count still-running))
        new-running (concat still-running (take free-cores not-expired))
        still-ables (drop free-cores not-expired)
        staggered (concat (:staggered state) finished)
        aborted (concat (:aborted state) expired)]
    (assoc state :ables still-ables :running new-running :staggered staggered :aborted aborted)))
