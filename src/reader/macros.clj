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
