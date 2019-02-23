(ns reader.macros)

(defmacro unless [expr form]
  (list 'if expr nil form))

;; (macroexpand-1 '(unless false (println "This should print.")))
