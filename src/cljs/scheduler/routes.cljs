(ns scheduler.routes
  (:require
    [reagent.core :refer [atom]]
    [scheduler.pages.home :as home]
    [accountant.core :as accountant]
    [secretary.core :as secretary :include-macros true]))

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
