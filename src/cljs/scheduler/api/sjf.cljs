(ns scheduler.api.sjf
  (:require [scheduler.api.process :as p]))

(defn sort-processes [processes]
  (sort-by :time processes))
  
(defn create-processes! []
  (->> (range (rand-int 50))
    (map p/create-process!)
    (vec)
    (sort-processes)))

(defn push-process [processes process]
  (-> processes
    (conj process)
    (sort-processes)))

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
    (assoc state :ables still-ables :running new-running :staggered staggered)))
