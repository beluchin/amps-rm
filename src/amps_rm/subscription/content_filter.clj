(ns amps-rm.subscription.content-filter
  (:refer-clojure :exclude [and or])
  (:require [clojure.set :as set]
            [clj-helpers :as h]))

(defprotocol ToString
  (to-string [this] "the string that can be used on a subscription as the content filter"))
(extend-protocol ToString
  String
  (to-string [this] this))

(defrecord ^:private And [lhs rhs]
  ToString
  (to-string [_] (format "(%s) AND (%s)" (to-string lhs) (to-string rhs))))

(defrecord ^:private Or [lhs rhs]
  ToString
  (to-string [_] (format "(%s) OR (%s)" (to-string lhs) (to-string rhs))))

(defn- try-to-recombine-or-into-and [and1 and2]
  (let [set1 #{(:lhs and1) (:rhs and1)}
        set2 #{(:lhs and2) (:rhs and2)}
        intersection (set/intersection set1 set2)]
    (if (seq intersection)
      (->And (h/single intersection)
             (->Or (h/single (set/difference set1 intersection))
                   (h/single (set/difference set2 intersection))))
      (->Or and1 and2))))

(defn and [cf1 cf2]
  (->And cf1 cf2))

(defn or [cf1 cf2]
  (if (clojure.core/and (instance? And cf1) (instance? And cf2))
    (try-to-recombine-or-into-and cf1 cf2)
    (->Or cf1 cf2)))
