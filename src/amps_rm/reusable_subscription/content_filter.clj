(ns amps-rm.reusable-subscription.content-filter
  (:refer-clojure :exclude [and or remove])
  (:require [clojure.set :as set]))

(defprotocol StringForm
  (string-form [cf] "the string that can be used on a subscription as the content filter"))
(extend-protocol StringForm
  String
  (string-form [cf] cf))

(defrecord ^:private And [operand-set]
  StringForm
  (string-form [_] (->> operand-set
                        (map #(format "(%s)" (string-form %)))
                        (String/join " AND "))))

(defrecord ^:private Or [operand-set]
  StringForm
  (string-form [_] (->> operand-set
                        (map #(format "(%s)" (string-form %)))
                        (String/join " OR "))))

(defn- try-to-recombine-or-into-and [and-set]
  (->Or and-set)
  #_(let [sets (map :operand-set and-set)
          intersection (apply set/intersection sets)]
      (if (seq intersection)
        (->And (h/single intersection)
               (->Or (h/single (set/difference set1 intersection))
                     (h/single (set/difference set2 intersection))))
        (->Or and1 and2))))

(declare or)
(defn add [cf1 cf2]
  (or cf1 cf2))

(defn and [& [_ _ & _ :as args]]
  (let [non-nil-args (set (filter some? args))]
    (when (seq non-nil-args)
      (if (= 1 (count non-nil-args))
        (first non-nil-args)
        (->And non-nil-args)))))

(defn or [& [_ _ & _ :as args]]
  (let [non-nil-args (set (filter some? args))]
    (when (seq non-nil-args)
      (if (= 1 (count non-nil-args))
        (first non-nil-args)
        (if (every? #(instance? And %) non-nil-args)
          (try-to-recombine-or-into-and non-nil-args)
          (->Or non-nil-args))))))

(defn remove [cf to-remove])
