(ns scheduler.state
  (:require [reagent.core :as r]
            [scheduler.api.core :as s]))

(defonce app-state (r/atom {:running? false
                            :scheduler nil
                            :ables []
                            :staggered []
                            :algorithm "sjf"
                            :running []
                            :quantum 0
                            :cores 0}))

(defn- starter [state func]
  (let [alg (:algorithm state)
        new-state (func state)
        scheduler (s/get-scheduler alg)
        create-processes! (s/get-processes-creator alg)]
    (assoc state :running? true :ables (create-processes!) :scheduler scheduler)))

(defn start [func]
  (try
    (swap! app-state #(starter % func))
    (catch :default e
      (println e))))

(defn run-scheduler [state]
  (let [scheduler (:scheduler state)]
    (if scheduler
      (scheduler state)
      (throw (js/Error.)))))

(defonce timer
  (let [tick (fn []
                (try                
                  (swap! app-state run-scheduler)
                  (catch :default e nil)))]
    (js/setInterval tick 1000)))
