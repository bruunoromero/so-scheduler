(ns scheduler.api.core
  (:require [scheduler.api.sjf :as sjf]))

(defonce schedulers 
  {"sjf" sjf/scheduler})

(defonce processes-creator
  {"sjf" sjf/create-processes!})

(defn get-scheduler [key]
  (get schedulers key))

(defn get-processes-creator [key]
  (get processes-creator key))