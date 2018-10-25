(ns scheduler.components.settings
  (:require
    [scheduler.state :as state]
    [scheduler.components.process :as p]))

(defn- select-algorithm []
  [:div.form-group
   [:label "Selecione um algoritimo"]
   [:select.form-control {:value (:algorithm @state/app-state) :on-change state/select-algorithm}
    [:option {:value "sjf"} "Shortest Job First"]
    [:option {:value "round-robin"} "Round Robin"]
    [:option {:value "ltg"} "Least Time to Go"]]])

(defn- cores-number []
  [:div.form-group
   [:label "Informe o número de cores"]
   [:input.form-control {:type     :number
                         :value    (:cores @state/app-state)
                         :on-input (state/set-value :cores)}]])

(defn- processes-number []
  [:div.form-group
   [:label "Informe o número de processos"]
   [:input.form-control {:type     :number
                         :value    (:cores @state/app-state)
                         :on-input (state/set-value :cores)}]])

(defn start-row [start]
  [:div.row.form-group
   [:div.col-md-12
    [:button.btn.btn-primary.btn-block
     {:on-click start}
     "Executar"]]])

(defn started-row []
  [:div.row.form-group
   [:div.col-md-6
    [:button.btn.btn-primary.btn-block "Adcionar processo"]]
   [:div.col-md-6
    [:button.btn.btn-danger.btn-block "Parar"]]])

(defn settings [start]
  [:main.col-md-12
   [:div.form.settings-row
    [:div.row
     [:div.col-md-6
      [select-algorithm]]
     [:div.col-md-6
      [cores-number]]]
    [:div.row
     [:div.col-md-6
      [processes-number]]
     [:div.col-md-6
      [processes-number]]]
    (if (state/running?)
      (started-row)
      (start-row start))]])
