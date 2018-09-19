(ns scheduler.pages.home
  (:require [cljs-time.coerce :as c]
            [cljs-time.core :as time]
            [scheduler.utils :as utils]
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

(defn process [process state]
  [:div {:key (:id process) :class (str "process " (name state))} 
    [:div
      [:span "id: "]
      [:span (str (:id process))]]
    [:div
      [:span "Estado: "]
      [:span (str (name state))]]
    [:div
      [:span "Passou: "]
      [:span (str (:elapsed process))]]
    [:div
      [:span "Tempo: "]
      [:span (str (:time process))]]
    [:div
      [:span "Expira em: "]
      [:span (c/to-string (:deadline process))]]
    [:div
      [:span "Agora: "]
      [:span (c/to-string (time/now))]]
    [:div
      [:span "igual: "]
      [:span (time/equal? (time/now) (:deadline process))]]
    [:div
      [:span "Abortado: "]
      [:span (if (:aborted? process) "Sim" "Não")]]])

(defn cores-row []
  [:div.cores-row.scrollable-x
    (for [running (:running @state/app-state)]
      (process running :running))])

(defn ables-row []
  [:div.processes-row.scrollable-x
    (for [able (:ables @state/app-state)] 
      (process able :able))])

(defn staggered-row []
  [:div.staggered-row.scrollable-x
    (for [staggered (:staggered @state/app-state)]
      (process staggered :staggered))])

(defn home-page []
  (fn []
    [:div.home-container
      [:div.home-column.cpu-column
        (cores-row)
        (ables-row)
        (staggered-row)]
      (to-start-column)]))