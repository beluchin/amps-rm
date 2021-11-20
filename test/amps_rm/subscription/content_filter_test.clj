(ns amps-rm.subscription.content-filter-test
  (:require [amps-rm.subscription.content-filter :as sut]
            [clojure.test :as t]))

(extend-protocol sut/ToString
  clojure.lang.Keyword
  (to-string [this] (name this)))

(t/deftest and-test
  (t/testing "to string"
    (t/is (= "(/a=1) AND (/b=2)" (sut/to-string (sut/and "/a=1" "/b=2"))))
    (t/is (= "(a) AND (b)" (sut/to-string (sut/and :a :b))))))

(t/deftest or-test
  (t/testing "to string"
    (t/is (= "(/a=1) OR (/b=2)" (sut/to-string (sut/or "/a=1" "/b=2")))))

  (t/testing "recombines and's"
    (t/is (= "(/a=1) AND ((/b=2) OR (/c=3))"
             (sut/to-string (sut/or (sut/and "/a=1" "/b=2")
                                    (sut/and "/a=1" "/c=3"))))))

  (t/testing "or-ing ands with no common expression"
    (t/is (= "((a) AND (b)) OR ((c) AND (d))"
             (sut/to-string (sut/or (sut/and :a :b)
                                    (sut/and :c :d))))))

  (t/testing "or-ing two identical and's"
    (t/is (= (sut/and :a :b) (sut/or (sut/and :a :b)
                                     (sut/and :a :b))))))
