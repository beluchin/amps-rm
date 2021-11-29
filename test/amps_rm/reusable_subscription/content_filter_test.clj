(ns amps-rm.reusable-subscription.content-filter-test
  (:require [amps-rm.reusable-subscription.content-filter :as sut]
            [clojure.test :as t]))

(t/deftest and-test
  (t/testing "string form"
    (t/is (#{"(/a=1) AND (/b=2)"
             "(/b=2) AND (/a=1)"}
            (sut/string-form (sut/and "/a=1" "/b=2")))))

  (t/testing "nil"
    (t/is (= "/a=1" (sut/and "/a=1" nil))))

  (t/testing "commutative"
    (t/is (= (sut/and "/a=1" "/b=2") (sut/and "/b=2" "/a=1"))))

  (t/testing "dedup"
    (t/is (= "/a=1" (sut/string-form (sut/and "/a=1" "/a=1"))))))

(t/deftest add-test
  (t/testing "basically an or"
    (t/is (#{"(/a=1) OR (/b=2)"
             "(/b=2) OR (/a=1)"}
            (sut/string-form (sut/add "/a=1" "/b=2")))))

  (t/testing "commutative"
    (t/is (= (sut/add "/a=1" "/b=2") (sut/add "/b=2" "/a=1"))))  

  (t/testing "recombines and's"
    (t/is (= (sut/and "/a=1" (sut/add "/b=2" "/c=3"))
             (sut/add (sut/and "/a=1" "/b=2")
                      (sut/and "/a=1" "/c=3")))))

  (t/testing "turns or's into in's"
    (throw (UnsupportedOperationException.)))

  (t/testing "nil"
    (t/is (nil? (sut/add "/a=1" nil)))))

(t/deftest string-form-test
  (t/is (= "/a=1" (sut/string-form "/a=1"))))
