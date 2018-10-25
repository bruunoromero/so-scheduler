(ns scheduler.pages.home
  (:require
    [scheduler.state :as state]
    [scheduler.components.memory :as m]
    [scheduler.components.process :as p]
    [scheduler.components.settings :as s]))

(defn try-start [state & params]
  (if (and (> (:cores state) 0)
           (or (not= (:algorithm state) "round-robin")
               (> (:quantum state) 0)))
    (apply assoc state params)
    (throw (js/Error. "Cannot start if cores are lower than 1"))))

(defn start []
  (state/start try-start))

(defn to-start-column []
  [:div.home-column.settings-column
    [:div.form-group
      [:label "Selecione um algoritimo"]
      [:select.form-control {:value (:algorithm @state/app-state) :on-change state/select-algorithm}
        [:option {:value "sjf"} "Shortest Job First"]
        [:option {:value "round-robin"} "Round Robin"]
        [:option {:value "ltg"} "Least Time to Go"]]]
    [:div.form-group
      [:label "Informe o número de cores"]
      [:input.form-control {:type :number
                            :value (:cores @state/app-state)
                            :on-input (state/set-value :cores)}]]
    (when (= (:algorithm @state/app-state) "round-robin")
      [:div.form-group
        [:label "Informe o número de quantum"]
        [:input.form-control {:type :number
                              :value (:quantum  @state/app-state)
                              :on-input (state/set-value :quantum)}]])
    [:div.form-group
      [:button.btn.btn-primary {:on-click start} "Executar"]]])

(defn update-column []
  [:div.home-column.settings-column
    [:div.form-group
      [:button.btn.btn-primary {:on-click state/push-process} "Adcionar processo"]]
    [:div.form-group
      [:button.btn.btn-danger {:on-click state/stop} "Parar"]]])

(defn cores-row []
  [:div.cores-row.scrollable-x
    (for [running (:running @state/app-state)]
      (p/process running :running))])

(defn ables-row []
  [:div.processes-row.scrollable-x
    (for [able (:ables @state/app-state)]
      (p/process able :able))])

(defn staggered-row []
  [:div.staggered-row.scrollable-x
    (for [staggered (:staggered @state/app-state)]
      (p/process staggered :staggered))])

(defn aborted-row []
  [:div.aborted-row.scrollable-x
    (for [aborted (:aborted @state/app-state)]
      (p/process aborted :aborted))])

(defn home-page []
  (fn []
    [:div.container
     [s/settings start]
     [:div.col-md.12.row
      [p/processes]
      [m/memory]]]))