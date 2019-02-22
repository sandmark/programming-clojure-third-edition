;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.test.multimethods.service-charge-1
  (:use clojure.test examples.multimethods.service-charge-1 examples.multimethods.account))

(alias 'acc 'examples.multimethods.account)

(deftest test-service-charge
  (are [x y] (= x y) 
   (service-charge {:tag ::acc/checking, :balance 4999}) 25
   (service-charge {:tag ::acc/checking, :balance 5000}) 0
   (service-charge {:tag ::acc/savings, :balance 999}) 10
   (service-charge {:tag ::acc/savings, :balance 1000}) 0))

  
