(ns ifskel.devcards
  (:require
   [devcards.core :as dc]
   [ifskel.client :as client]
   [reagent.core :as r])
  (:require-macros
   [cljs.test :refer [testing is]]
   [devcards.core :refer [defcard deftest defcard-rg]]))

(dc/start-devcard-ui!)

(defcard my-first-card
  "##My very first card

Look at me, I am markdown!

    (+ 17 4)
")

(def counter-state (r/atom 0))

(defcard-rg counter-card
  [client/counter-component counter-state]
  counter-state
  {:inspect-data true :history true})


(defcard-rg hello-card
  "This shows how to only pass the state once.

(counter-card needs the state atom twice)."

  (fn [hello-state _]
    [client/hello-component hello-state])
  (r/atom "world!")
  {:inspect-data true :history true})

(deftest some-test
  "##Does it work? Does it?"
  (testing "it would be nice if it would work"
    (is (= 1 0)))
  "Some more markdown which is always nice"
  (testing "This one seems ok"
    (is (= 0 0))))
