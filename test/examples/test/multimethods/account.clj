;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.test.multimethods.account
  (:use clojure.test examples.multimethods.account))

(alias 'acc 'examples.multimethods.account)

(deftest test-interest-rate
  (are [x y] (= x y)
   (interest-rate {:tag ::acc/checking}) 0M
   (interest-rate {:tag ::acc/savings}) 0.05M))

(deftest test-account-level
  (are [x y] (= x y)
   (account-level {:tag ::acc/checking, :balance 4999}) ::acc/basic
   (account-level {:tag ::acc/checking, :balance 5000}) ::acc/premium
   (account-level {:tag ::acc/savings, :balance 999}) ::acc/basic
   (account-level {:tag ::acc/savings, :balance 1000}) ::acc/premium))

  
