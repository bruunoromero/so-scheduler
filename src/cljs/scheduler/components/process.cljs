(ns scheduler.components.process
  (:require
    [cljs-time.coerce :as c]
    [scheduler.state :as state]))

(defn- table-head []
  [:thead
   [:tr
    [:th "#"]
    [:th "Estado"]
    [:th "Passou"]
    [:th "Tempo"]
    [:th "Expira"]]])

(defn- process-row [process]
  [:tr
   [:th (:id process)]
   [:td (:status process)]
   [:td (:elapsed process)]
   [:td (:time process)]
   [:td (:deadline (c/to-string (:deadline process)))]])

(defn table [title processes]
  [:div.card
   [:div.card-body
    [:h5.card-title title]
    [:table.table
     (table-head)
     [:tbody
      (for [process processes]
        [process-row process])]]]])

(defn processes []
  [:div.col-md-6
   [table "Escalonando" (:running @state/app-state)]
   [table "Aptos" (:ables @state/app-state)]
   [table "Escalonados" (:staggered @state/app-state)]
   [table "Abortados" (:aborted @state/app-state)]])