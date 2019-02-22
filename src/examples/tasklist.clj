;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.tasklist
  (:gen-class 
   :extends org.xml.sax.helpers.DefaultHandler
   :init init
   :state state)
  (:require [clojure.java.io :refer (reader)])
  (:import [java.io File]
           [org.xml.sax InputSource]
	   [org.xml.sax.helpers DefaultHandler]
	   [javax.xml.parsers SAXParserFactory]))

(defn task-list [arg]
  (let [handler (new examples.tasklist)] 
    (.. SAXParserFactory newInstance newSAXParser
	(parse (InputSource. (reader (File. arg)))
	       handler))
    @(.state handler))) 

(defn -init []
  [[] (atom [])])

(defn -startElement
  [this uri local qname atts]
  (when (= qname "target")
    (swap! (.state this) conj (.getValue atts "name")))) 

(defn -main [& args]
  (doseq [arg args]
    (println (task-list arg))))
