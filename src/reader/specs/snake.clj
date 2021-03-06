(ns reader.specs.snake
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [examples.import-static :as import-static]
            [reader.snake :as snake])
  (:import java.awt.Color))

(import-static/import-static java.awt.event.KeyEvent VK_LEFT VK_RIGHT VK_UP VK_DOWN)

(s/def ::point
  (s/tuple (s/int-in (- snake/width) snake/width)
           (s/int-in (- snake/width) snake/height)))

(s/def ::rectangle
  (s/coll-of int? :count 4))

(s/def ::color (partial instance? Color))
(s/def ::type #{:apple :snake})
(s/def ::location ::point)
(s/def ::body (s/and list? (s/coll-of ::point)))
(s/def ::dir (s/or :left  (partial = (get snake/dirs VK_LEFT))    ; HACK
                   :right (partial = (get snake/dirs VK_RIGHT))
                   :up    (partial = (get snake/dirs VK_UP))
                   :down  (partial = (get snake/dirs VK_DOWN))))

(s/def ::game-object
  (s/keys :req-un [::color ::type (or ::location (and ::body ::dir))]))

(s/fdef snake/add-points
  :args (s/+ ::point)
  :ret  (s/tuple int? int?))

(s/fdef snake/point-to-screen-rect
  :args (s/cat :pt ::point)
  :ret  ::rectangle)

(s/fdef snake/create-apple
  :args (s/cat)
  :ret ::game-object)

(s/fdef snake/create-snake
  :args (s/cat)
  :ret ::game-object)

(comment
  (stest/instrument)
  (stest/check)
  (stest/check 'reader.snake/add-points)
  (stest/check 'reader.snake/point-to-screen-rect)
  (stest/check 'reader.snake/create-apple)
  (stest/check 'reader.snake/create-snake))
