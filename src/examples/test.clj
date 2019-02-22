;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.test
  (:require [clojure.test :refer :all]))

(def examples-tests 
     (map #(symbol (str "examples.test." (name %)))
	  [:chat :exploring :interop :introduction :multimethods :preface
	    :multimethods.account :multimethods.service-charge-1
	    :multimethods.service-charge-2 :multimethods.service-charge-3
	    :sequences :index-of-any :life-without-multi :multimethods.default
	    :macros :macros.chain-1 :macros.chain-2 :macros.chain-3 :macros.chain-4
	    :macros.chain-5 :lazy-index-of-any :macros.bench-1
	    :concurrency :functional :snake :snippet :replace-symbol 
	    :wallingford :trampoline :tasklist
	    :male-female :memoized-male-female :male-female-seq]))

(def lancet-tests 
     (map #(symbol (str "lancet.test." (name %)))
	  [:step-1-repl :step-1-complete
	    :step-2-repl :step-2-complete
	    :step-3-repl :step-3-complete
	    :step-4-repl :step-4-complete
	    :step-5-repl :step-5-complete
	    :deftarget-1]))

(def all-tests (concat examples-tests lancet-tests))

(doseq [test all-tests] (require test))

(apply run-tests all-tests)

(shutdown-agents)
