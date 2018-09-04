(ns scheduler.core
    (:require [reagent.core :as reagent]
              [scheduler.routes :as routes]))

(defn mount-root []
  (reagent/render [routes/current-page] (.getElementById js/document "app")))

(defn init! []
  (routes/setup-routes)
  (routes/dispatch-current!)
  (mount-root))
