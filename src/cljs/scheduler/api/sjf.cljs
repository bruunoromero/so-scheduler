(ns scheduler.api.sjf
  (:require [scheduler.api.process :as p]))

(defn sort-processes [processes]
  (sort-by :time processes))
  
(defn create-processes! []
  (->> (range (rand-int 10))
    (map p/create-process!)
    (sort-processes)
    (vec)))

(defn push-process [processes process]
  (-> processes
    (conj process)
    (sort-processes)))

(defn next-process [processes]
  (first processes))

(defn pop-process [processes process]
  (remove #(= (:id process) %) processes))