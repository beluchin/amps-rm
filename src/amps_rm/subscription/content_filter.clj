(ns amps-rm.subscription.content-filter
  (:refer-clojure :exclude [and or])
  (:require [amps-rm.subscription.content-filter.impl :as impl]))

(defn and [cf1 cf2]
  (impl/->And cf1 cf2))

(defn or [cf1 cf2])

(def to-string impl/to-string)
