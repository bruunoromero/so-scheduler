(ns scheduler.pages.home
  (:require [scheduler.utils :as utils]
            [scheduler.state :as state]))

(defn select-algorithm [e]
  (swap! state/app-state assoc :algorithm (utils/value e)))

(defn try-start [state & params]
  (if (and (> (:cores state) 0)
           (or (not= (:algorithm state) "round-robin")
               (> (:quantum state) 0)))
    (apply assoc state params)
    (throw (js/Error. "Cannot start if cores are lower than 1"))))

(defn start []
  (state/start try-start))

(defn set-value [key]
  #(swap! state/app-state assoc key (js/parseInt (utils/value %))))

(defn to-start-column []
  [:div.home-column.settings-column
    [:div.form-group
      [:label "Selecione um algoritimo"]
      [:select.form-control {:value (:algorithm @state/app-state) :on-change select-algorithm}
        [:option {:value "sjf"} "Shortest Job First"]
        [:option {:value "round-robin"} "Round Robin"]
        [:option {:value "ltg"} "Least Time to Go"]]]
    [:div.form-group
      [:label "Informe o número de cores"]
      [:input.form-control {:type :number
                            :value (:cores @state/app-state)
                            :on-input (set-value :cores)}]]
    (when (= (:algorithm @state/app-state) "round-robin")
      [:div.form-group
        [:label "Informe o número de quantum"]
        [:input.form-control {:type :number
                              :value (:quantum  @state/app-state)
                              :on-input (set-value :quantum)}]])
    [:div.form-group
      [:button.btn.btn-primary {:on-click start} "Executar"]]])

(defn ables-row []
  [:div.processes-row.scrollable-x
    (for [able (:ables @state/app-state)] [:div.process.able {:key (:id able)} (str (:time able))])])

(defn home-page []
  (fn []
    [:div.home-container
      [:div.home-column.cpu-column
        [:div.cores-row
          (for [running (:running @state/app-state)]
            [:div.process.able {:key (:id running)}
              [:div (str (:elapsed running))]
              [:div (str (:time running))]])]
        (ables-row)]
      (to-start-column)]))
      
          

