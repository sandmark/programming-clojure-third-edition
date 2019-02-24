(ns reader.macros)

(defmacro unless [expr form]
  (list 'if expr nil form))

;; (macroexpand-1 '(unless false (println "This should print.")))

(defmacro chain
  ([x form] (list '. x form))
  ([x form & more]
   (concat (list 'chain (list '. x form)) more)))

(macroexpand-1 '(chain arm getHand))
(macroexpand-1 '(chain arm getHand getFinger))
(macroexpand '(chain arm getHand getFinger))

(defmacro chain-incorrect
  ([x form] `(. ~x ~form))
  ([x form & more]
   `(chain-incorrect (. ~x ~form) ~more)))

(macroexpand-1  ; works fine with one argument
 '(chain-incorrect arm getHand))
(macroexpand-1  ; something goes wrong
 '(chain-incorrect arm getHand getFinger))
(macroexpand    ; completely broken
 '(chain-incorrect arm getHand getFinger))

(defmacro chain-splice
  ([x form] `(. ~x ~form))
  ([x form & more] `(chain-splice (. ~x ~form) ~@more)))

(macroexpand-1
 '(chain-splice arm getHand))
(macroexpand-1
 '(chain-splice arm getHand getFinger))
(macroexpand
 '(chain-splice arm getHand getFinger))
