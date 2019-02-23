(ns reader.specs.snake
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [reader.snake :as snake]))


(s/def ::point
  (s/tuple (s/int-in 0 snake/width)
           (s/int-in 0 snake/height)))

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
