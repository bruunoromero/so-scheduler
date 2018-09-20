(ns scheduler.api.core
  (:require [scheduler.api.sjf :as sjf]
            [scheduler.api.ltg :as ltg]
            [scheduler.api.round-robin :as round-robin]))

(defonce schedulers 
  {"sjf" sjf/scheduler
   "ltg" ltg/scheduler
   "round-robin" round-robin/scheduler})

(defonce processes-creator
  {"sjf" sjf/create-processes!
   "ltg" ltg/create-processes!
   "round-robin" round-robin/create-processes!})

(defonce process-pushers
  {"sjf" sjf/push-process
   "ltg" ltg/push-process
   "round-robin" round-robin/push-process})

(defn get-scheduler [key]
  (get schedulers key))

(defn get-processes-creator [key]
  (get processes-creator key))

(defn get-process-pusher [key]
  (get process-pushers key))
