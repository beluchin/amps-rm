(ns amps-rm.client-test
  (:require [amps-rm.client :as sut]
            [clojure.test :as t]))

(t/deftest ensure-test
  (t/testing "no client - promise is cached"
    (t/is (= {:uri :promise} (sut/ensure {} :uri :promise))))

  (t/testing "promise exists - do nothing"
    (t/is (= {:uri :existing} (sut/ensure {:uri :existing} :uri :new)))))
