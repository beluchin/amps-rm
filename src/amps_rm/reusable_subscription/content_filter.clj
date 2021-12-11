(ns amps-rm.reusable-subscription.content-filter
  (:refer-clojure :exclude [and or remove])
  (:require [clojure.set :as set]))

(defprotocol StringForm
  (string-form [cf] "the string that can be used on a subscription as the content filter"))
(extend-protocol StringForm
  String
  (string-form [cf] cf))

(defprotocol Remove
  (remove [cf to-remove]))

(defrecord ^:private And [operand-set])
(extend-type And
  StringForm
  (string-form [this] (->> (:operand-set this)
                        (map #(format "(%s)" (string-form %)))
                        (String/join " AND ")))
  Remove
  (remove [this to-remove] this))

(declare or try-to-recombine-or-into-and)
(defrecord ^:private Or [operand-set])
(extend-type Or
  StringForm
  (string-form [this]
    (let [operand-set (:operand-set this)
          recombined (try-to-recombine-or-into-and operand-set)]
      (if (not= recombined this)
        (string-form recombined)
        (->> operand-set
             (map #(format "(%s)" (string-form %)))
             (String/join " OR ")))))
  Remove
  (remove [this to-remove]
    (let [operand-set (:operand-set this)]
      (if (contains? operand-set to-remove)
        (apply or (clojure.core/remove #{to-remove} operand-set))
        this))))

(declare and)
(defn- try-to-recombine-or-into-and [ands]
  (let [sets (map :operand-set ands)
        intersection (apply set/intersection sets)]
    (if (seq intersection)
      (->And (conj intersection
                   (->Or (set/difference (apply set/union sets) intersection))))
      (->Or ands))))

(declare or)
(defn add [cf1 cf2]
  (or cf1 cf2))

(defn and
  ([x] x)
  ([x y & more]
   (let [non-nil-args (set (filter some? (conj more x y)))]
     (when (seq non-nil-args)
       (if (= 1 (count non-nil-args))
         (first non-nil-args)
         (->And non-nil-args))))))

(defn or
  ([x] x)
  ([x y & more]
   (let [non-nil-args (set (filter some? (conj more x y)))]
     (when (seq non-nil-args)
       (if (= 1 (count non-nil-args))
         (first non-nil-args)
         (->Or non-nil-args))))))
