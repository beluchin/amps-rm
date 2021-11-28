(ns amps-rm.reusable-subscription.content-filter
  (:refer-clojure :exclude [and remove]))

(defn add [cf1 cf2])

(defn and [cf1 cf2 & more])

(defn remove [cf to-remove])

(defn string-form [cf])
