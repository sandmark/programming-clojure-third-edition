;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.multimethods.account)

(alias 'acc 'examples.multimethods.account)

(defmulti interest-rate :tag)
(defmethod interest-rate ::acc/checking [_] 0M)
(defmethod interest-rate ::acc/savings [_] 0.05M)

(defmulti account-level :tag)
(defmethod account-level ::acc/checking [acct]
  (if (>= (:balance acct) 5000) ::acc/premium ::acc/basic))
(defmethod account-level ::acc/savings [acct]
  (if (>= (:balance acct) 1000) ::acc/premium ::acc/basic))

