(ns amps-rm.subscription.content-filter-test
  (:require [amps-rm.subscription.content-filter :as sut]
            [clojure.test :as t]))

(t/deftest and-test
  (t/testing "to string"
    (t/is (= "(/a=1) AND (/b=2)" (sut/to-string (sut/and "/a=1" "/b=2"))))))

