(ns scheduler.routes
  (:require [reagent.core :refer [atom]]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [scheduler.pages.home :as home]))

(defonce page (atom #'home/home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'home/home-page))

(defn dispatch-current! [] 
  (accountant/dispatch-current!))

(defn setup-routes []
  (accountant/configure-navigation!
    {:nav-handler
      (fn [path]
        (secretary/dispatch! path))
     :path-exists?
      (fn [path]
        (secretary/locate-route path))}))
