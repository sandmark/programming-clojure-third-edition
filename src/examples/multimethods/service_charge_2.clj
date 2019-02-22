;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.multimethods.service-charge-2
  (:require examples.multimethods.account))

(in-ns 'examples.multimethods.account)
(clojure.core/use 'clojure.core)

(defmulti service-charge (fn [acct] [(account-level acct) (:tag acct)]))
(defmethod service-charge [::acc/basic ::acc/checking]   [_] 25)
(defmethod service-charge [::acc/basic ::acc/savings]    [_] 10)
(defmethod service-charge [::acc/premium ::acc/checking] [_] 0)
(defmethod service-charge [::acc/premium ::acc/savings]  [_] 0)
