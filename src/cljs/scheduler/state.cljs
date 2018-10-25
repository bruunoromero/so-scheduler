(ns scheduler.state
  (:require
    [reagent.core :as r]
    [scheduler.api.core :as s]
    [scheduler.utils :as utils]
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

(defn set-value [key]
  #(swap! app-state assoc key (js/parseInt (utils/value %))))

(defn select-algorithm [e]
  (swap! app-state assoc :algorithm (utils/value e)))

(defn- starter [state func]
  (let [alg (:algorithm state)
        scheduler (s/get-scheduler alg)
        create-processes! (s/get-processes-creator alg)]
    (assoc (func state) :running? true :ables (create-processes!) :scheduler scheduler)))

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

(defn running? []
  (:running? app-state))

(defn push-process []
  (swap! app-state process-pusher))

(defn update-status [status]
  #(assoc % :status status))

(defn update-statuses [state]
  (-> state
    (update-in [:running] #(map (update-status :escalonando) %))
    (update-in [:ables] #(map (update-status :esperando) %))
    (update-in [:staggered] #(map (update-status :escalonado) %))
    (update-in [:aborted] #(map (update-status :abortado) %))))

(defn run-scheduler [state]
  (let [scheduler (:scheduler state)
        running? (:running? state)]
    (if (and scheduler running?)
      (->> state
        (scheduler)
        (update-statuses))
      (throw (js/Error.)))))

(defonce timer
  (let [tick (fn []
                (try
                  (swap! app-state run-scheduler)
                  (catch :default e nil)))]
    (js/setInterval tick 1000)))
