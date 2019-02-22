;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
;; NOT for execution. These are excerpts from Clojure
;; and are subject to the Clojure License, repeated below:

;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Common Public License 1.0 (http://opensource.org/licenses/cpl.php)
;   which can be found in the file CPL.TXT at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(defmacro and
  ([] true)
  ([x] x)
  ([x & rest]
     `(let [and# ~x]    
      (if and# (and ~@rest) and#)))) 

(defmacro with-out-str
  [& body]  
  `(let [s# (new java.io.StringWriter)] 
     (binding [*out* s#] 
       ~@body            
       (str s#))))       

(defmacro defstruct
  [name & keys]
  `(def ~name (create-struct ~@keys)))

(defmacro declare
  [& names] `(do ~@(map #(list 'def %) names)))
