(ns scheduler.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [scheduler.core-test]))

(doo-tests 'scheduler.core-test)
