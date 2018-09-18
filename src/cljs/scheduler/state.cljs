(ns scheduler.state
  (:require [reagent.core :as r]))

(defonce app-state (r/atom {:running false
                            :scheduler nil
                            :ables []
                            :staggered []
                            :algorithm "ltg"
                            :quantum 0
                            :cores 0}))

(defn start [func processes]
  (try
    (swap! app-state func :running true :ables processes)
    (catch :default e
      (println "ERR"))))

(defn scheduler []
  (.log js/console "helloo"))

(defonce timer
  (js/setInterval scheduler 1000))