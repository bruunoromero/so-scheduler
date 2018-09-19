(ns scheduler.api.core
  (:require [scheduler.api.sjf :as sjf]
            [scheduler.api.ltg :as ltg]))

(defonce schedulers 
  {"sjf" sjf/scheduler
   "ltg" ltg/scheduler})
(defonce processes-creator
  {"sjf" sjf/create-processes!
   "ltg" sjf/create-processes!})

(defn get-scheduler [key]
  (get schedulers key))

(defn get-processes-creator [key]
  (get processes-creator key))