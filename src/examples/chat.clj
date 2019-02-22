;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.chat)

(defrecord Message [sender text])

(def messages (ref ()))

(defn valid-message? [msg] 
  (and (:sender msg) (:text msg)))

(def validate-message-list #(every? valid-message? %))

(def messages (ref () :validator validate-message-list))

; bad idea
(defn naive-add-message [msg]
  (dosync (ref-set messages (cons msg @messages))))
 
(defn add-message [msg]
  (dosync (alter messages conj msg)))

(defn add-message-commute [msg]
  (dosync (commute messages conj msg)))
