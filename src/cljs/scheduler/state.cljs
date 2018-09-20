(ns scheduler.state
  (:require [reagent.core :as r]
            [scheduler.api.core :as s]
            [scheduler.api.process :as p]))

(defonce ^{:private true} initial-state
  {:running? false
   :scheduler nil
   :ables []
   :staggered []
   :aborted []
   :algorithm "sjf"
   :running []
   :quantum 0
   :cores 0})

(defonce app-state (r/atom initial-state))

(defn- starter [state func]
  (let [alg (:algorithm state)
        new-state (func state)
        scheduler (s/get-scheduler alg)
        create-processes! (s/get-processes-creator alg)]
    (assoc state :running? true :ables (create-processes!) :scheduler scheduler)))

(defn- process-pusher [state]
  (let [alg (:algorithm state)
        pusher (s/get-process-pusher alg)]
    (update-in state [:ables] pusher (p/create-process!))))

(defn start [func]
  (try
    (swap! app-state #(starter % func))
    (catch :default e
      (println e))))

(defn stop []
  (reset! app-state initial-state))

(defn push-process []
  (swap! app-state process-pusher))

(defn run-scheduler [state]
  (let [scheduler (:scheduler state)
        running? (:running? state)]
    (if (and scheduler running?)
      (scheduler state)
      (throw (js/Error.)))))

(defonce timer
  (let [tick (fn []
                (try                
                  (swap! app-state run-scheduler)
                  (catch :default e nil)))]
    (js/setInterval tick 1000)))
