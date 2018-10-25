(ns scheduler.api.round-robin
  (:require [scheduler.api.process :as p]))

(defn create-processes! []
  (->> (range (rand-int 50))
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
        not-finished (filter #(not= (:elapsed %) (:time %)) up-running)
        to-ables (filter #(= 0 (mod (:elapsed %) (:quantum state))) not-finished)
        still-running (filter #(not= 0 (mod (:elapsed %) (:quantum state))) not-finished)
        free-cores (- (:cores state) (count still-running))
        still-ables (drop free-cores ables)
        up-ables (concat still-ables to-ables)
        new-running (concat still-running (take free-cores ables))
        staggered (concat (:staggered state) finished)]
    (println (map #(:id %) finished))
    (assoc state :ables up-ables :running new-running :staggered staggered)))
