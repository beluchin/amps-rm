(ns amps-rm.reusable-subscription.content-filter
  (:refer-clojure :exclude [and remove]))

(defprotocol StringForm
  (string-form [cf] "the string that can be used on a subscription as the content filter"))
(extend-protocol StringForm
  String
  (string-form [cf] cf))

(defrecord ^:private And [operands]
  StringForm
  (string-form [_] (->> operands
                        (map #(format "(%s)" (string-form %)))
                        (String/join " AND "))))

(defrecord ^:private Or [operands])

(defn add [cf1 cf2])

(defn and [cf1 cf2 & more]
  (->And (into more [cf2 cf1])))

(defn remove [cf to-remove])
