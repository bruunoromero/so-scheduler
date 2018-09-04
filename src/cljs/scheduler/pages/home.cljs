(ns scheduler.pages.home
  (:require [reagent.core :as r]
            [scheduler.api.sjf :as sjf]))

(defonce app-state (r/atom {:running false :processes '() :algorithm :ltg}))

(defn select-algorithm [e]
  (let [value (.-value (.-target e))]
    (swap! app-state assoc-in [:algorithm] value)))

(defn start [e]
  (doto app-state
    (swap! assoc-in [:running] true)
    (swap! assoc-in [:processes] (sjf/create-processes!))))

(defn to-start-column []
  [:div.home-column.settings-column
    [:div.form-group
      [:label "Selecione um algoritimo"]
      [:select.form-control {:value (:algorithm @app-state) :on-change select-algorithm}
        [:option {:value :sjf} "Shortest Job First"]
        [:option {:value :round-robin} "Round Robin"]
        [:option {:value :ltg} "Least Time to Go"]]]
    [:div.form-group
      [:button.btn.btn-primary {:on-click start} "Executar"]]])

(defn home-page []
  [:div.home-container
    [:div.home-column.cpu-column
      [:div.cores-row (str @app-state)]
      [:div.processes-row]]
    (to-start-column)])
      
          

