(ns reader.specs.snake
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [reader.snake :as snake]))

(defn position-int?
  "Returns true if `n` is a valid position number."
  [n]
  (let [m (max snake/height snake/width)]
    (<= (- m) n m)))

(s/def ::position
  (s/and int? position-int?))

(s/def ::point
  (s/tuple ::position ::position))

(s/def ::rectangle
  (s/coll-of int? :count 4))

(s/fdef snake/add-points
  :args (s/+ ::point)
  :ret  (s/tuple int? int?))

(s/fdef snake/point-to-screen-rect
  :args (s/cat :pt ::point)
  :ret  ::rectangle)

(stest/instrument (stest/enumerate-namespace 'reader.snake))

(comment
  (stest/check 'reader.snake/add-points)
  (stest/check 'reader.snake/point-to-screen-rect))
