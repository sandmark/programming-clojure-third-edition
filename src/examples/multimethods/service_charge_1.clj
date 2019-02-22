;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.multimethods.service-charge-1
  (:require examples.multimethods.account))

; namespace hackery for segregating multiple service-charge impls
(in-ns 'examples.multimethods.account)
(clojure.core/use 'clojure.core)

; bad approach
(defmulti service-charge account-level)
(defmethod service-charge ::basic [acct]
  (if (= (:tag acct) ::checking) 25 10))
(defmethod service-charge ::premium [_] 0)

