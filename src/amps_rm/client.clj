(ns amps-rm.client
  (:refer-clojure :exclude [ensure promise]))


;; This is the implementation of the single-client client manager
;; However, this API could be used to implement other approaches
;; e.g. client pool; new client per invocation; ...


(defn disconnected [cmgr uri])
(defn failed-to-connect [cmgr uri])

(defn ensure [cmgr uri promise]
  (assoc cmgr uri promise))

(defn promise [cmgr uri])
