(ns reader.macros)

(defmacro unless [expr form]
  (list 'if expr nil form))
