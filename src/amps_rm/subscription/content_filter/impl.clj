(ns amps-rm.subscription.content-filter.impl)

(defprotocol ToString
  (to-string [this] "the string that can be used on a subscription as the content filter"))

(defrecord And [lhs rhs]
  ToString
  (to-string [_] (format "(%s) AND (%s)" (to-string lhs) (to-string rhs))))

(extend-protocol ToString
  String
  (to-string [this] this))
