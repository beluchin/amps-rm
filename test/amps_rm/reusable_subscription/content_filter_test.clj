(ns amps-rm.reusable-subscription.content-filter-test
  (:require [amps-rm.reusable-subscription.content-filter :as sut]
            [clojure.test :as t]))

(extend-protocol sut/StringForm
  clojure.lang.Keyword
  (string-form [this] (name this)))

(t/deftest and-test
  (t/testing "string form"
    (t/is (#{"(/a=1) AND (/b=2)"
             "(/b=2) AND (/a=1)"}
            (sut/string-form (sut/and "/a=1" "/b=2")))))

  (t/testing "nil"
    (t/is (= "/a=1" (sut/and "/a=1" nil)))
    (t/is (= "/a=1" (sut/and nil "/a=1"))))

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

  (t/testing "recombines and's if possible"
    (t/is (= (sut/and :a (sut/or :b :c))
             (sut/add (sut/and :a :b)
                      (sut/and :a :c))))
    (t/is (= (sut/and :a :b (sut/add :c :d))
             (sut/add (sut/and :a :b :c)
                      (sut/and :a :b :d))))
    (t/is (= "((c) AND (d)) OR ((b) AND (a))"
             (sut/string-form (sut/or (sut/and :a :b) (sut/and :c :d))))))

  #_(t/testing "turns or's into in's"
    (throw (UnsupportedOperationException.)))

  (t/testing "nil"
    (t/is (= "/a=1" (sut/add "/a=1" nil)))
    (t/is (= "/a=1" (sut/add nil "/a=1")))))

(t/deftest string-form-test
  (t/is (= "/a=1" (sut/string-form "/a=1"))))
