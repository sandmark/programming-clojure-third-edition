;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.test.macros.chain-1
  (:use clojure.test examples.macros.chain-1))

(deftest test-chain-1
  (are [x y] (= x y)
   (macroexpand-1 '(examples.macros.chain-1/chain a b)) '(. a b))
  (is (thrown? IllegalArgumentException (macroexpand-1 '(examples.macros.chain-1/chain a b c)))))
