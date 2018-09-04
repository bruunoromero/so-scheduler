(ns ^:figwheel-no-load scheduler.dev
  (:require
    [scheduler.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
