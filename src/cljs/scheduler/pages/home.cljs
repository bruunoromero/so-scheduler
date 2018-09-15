(ns scheduler.pages.home
  (:require [reagent.core :as r]
            [scheduler.api.sjf :as sjf]))

(defonce app-state (r/atom {:running false 
                            :ables []
                            :staggered []
                            :algorithm "ltg"
                            :quantum 0
                            :cores 0}))

(defn value [e]
  (.-value (.-target e)))

(defn select-algorithm [e]
  (swap! app-state assoc :algorithm (value e)))

(defn try-start [state & params]
  (if (and (> (:cores state) 0)
           (or (not= (:algorithm state) "round-robin")
               (> (:quantum state) 0)))
    (apply assoc state params)
    (throw (js/Error. "Cannot start if cores are lower than 1"))))

(defn set-value [key]
  #(swap! app-state assoc key (js/parseInt (value %))))

(defn start [e]
  (try
    (swap! app-state try-start :running true :ables (sjf/create-processes!))
    (catch :default e
      (println "ERR"))))

(defn to-start-column []
  [:div.home-column.settings-column
    [:div.form-group
      [:label "Selecione um algoritimo"]
      [:select.form-control {:value (:algorithm @app-state) :on-change select-algorithm}
        [:option {:value "sjf"} "Shortest Job First"]
        [:option {:value "round-robin"} "Round Robin"]
        [:option {:value "ltg"} "Least Time to Go"]]]
    [:div.form-group
      [:label "Informe o número de cores"]
      [:input.form-control {:type :number
                            :value (:cores @app-state)
                            :on-input (set-value :cores)}]]
    (when (= (:algorithm @app-state) "round-robin")
      [:div.form-group
        [:label "Informe o número de quantum"]
        [:input.form-control {:type :number
                              :value (:quantum  @app-state)
                              :on-input (set-value :quantum)}]])
    [:div.form-group
      [:button.btn.btn-primary {:on-click start} "Executar"]]])

(defn ables-row []
  [:div.processes-row.scrollable-x
    (for [able (:ables @app-state)] [:div.process.able "ola"])])

(defn home-page []
  [:div.home-container
    [:div.home-column.cpu-column
      [:div.cores-row (str @app-state)]
      (ables-row)]
    (to-start-column)])
      
          

