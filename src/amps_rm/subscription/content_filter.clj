(ns amps-rm.subscription.content-filter
  (:refer-clojure :exclude [and or]))

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

(defn and [cf1 cf2]
  (->And cf1 cf2))

(defn or [cf1 cf2]
  (->Or cf1 cf2))
