;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns reader.snake
  (:import (java.awt Color Dimension)
           (javax.swing JPanel JFrame Timer JOptionPane)
           (java.awt.event ActionListener KeyListener))
  (:require [examples.import-static :refer :all]))

(import-static java.awt.event.KeyEvent VK_LEFT VK_RIGHT VK_UP VK_DOWN)

(def width
  "The width of the game board."
  75)

(def height
  "The height of the game board."
  50)

(def point-size
  "Used to convert a game point into screen pixels."
  10)

(def turn-millis
  "The heartbeat of the game.
  It controlls how many milliseconds pass before each update of the game board."
  75)

(def win-length
  "Snake segments threshold that is needed to win the game."
  5)

(def dirs
  "Represents four directions as a map.
  It contains `VK_LEFT`, `VK_RIGHT`, `VK_UP`, and `VK_DOWN`, indicates
  their actual vectors equivalents."
  {VK_LEFT  [-1  0]
   VK_RIGHT [ 1  0]
   VK_UP    [ 0 -1]
   VK_DOWN  [ 0  1]})

(defn add-points
  "Adds points together.
  It can be used to calculate the new position of a moving game object."
  [& pts]
  (->> pts (apply map +) vec))

(defn point-to-screen-rect
  "Takes a `point` in game space and converts it to a rectagle on the screen.
  The result value is calculated with `point-size` value."
  [[pt1 pt2]]
  (map (partial * point-size) [pt1 pt2 1 1]))

(defn create-apple
  "Creates a new apple represented as a map.
  `:location` is a single point where apple occupied, which is randomly generated.
  `:color` is fixed default to `(Color. 210 50 90)`
  `:type` represents the object type, fixed to `:apple`."
  []
  {:location (vector (rand-int width) (rand-int height))
   :color    (Color. 210 50 90)
   :type     :apple})

(defn create-snake
  "Creates a new snake represented as a map.
  `:body` is a list of points where the snake occupied, defaults to ([1 1]).
  `:dir` is an direction which way the snake heading, defaults to [1 0]. See also `dirs`.
  `:color` is fixed default to `(Color. 15 160 70)`
  `type` represents the object type, fixed to `:snake`"
  []
  {:body  (list [1 1])
   :dir   [1 0]
   :color (Color. 15 160 70)
   :type  :snake})

(defn move [{:keys [body dir] :as snake} & grow]
  (assoc snake :body (cons (add-points (first body) dir)
                           (if grow body (butlast body)))))

(defn win? [{body :body}]
  (>= (count body) win-length))

(defn head-overlaps-body? [{[head & body] :body}]
  (contains? (set body) head))

(def lose? head-overlaps-body?)

(defn eats? [{[snake-head] :body}
             {apple :location}]
  (= snake-head apple))

(defn turn [snake newdir]
  (assoc snake :dir newdir))

(defn reset-game [snake apple]
  (dosync (ref-set apple (create-apple))
          (ref-set snake (create-snake)))
  nil)

(defn update-direction [snake newdir]
  (when newdir
    (dosync (alter snake turn newdir))))

(defn update-position [snake apple]
  (dosync
   (if (eats? @snake @apple)
     (do (ref-set apple (create-apple))
         (alter snake move :grow))
     (alter snake move)))
  nil)

(defn fill-point [g pt color]
  (let [[x y width height] (point-to-screen-rect)]
    (.setColor g color)
    (.fillRect g x y width height)))

(defmulti paint (fn [g object & _] (:type object)))

(defmethod paint :apple [g {:keys [location color]}]
  (fill-point g location color))

(defmethod paint :snake [g {:keys [body color]}]
  (doseq [point body]
    (fill-point g point color)))

(defn game-panel [frame snake apple]
  (proxy [JPanel ActionListener KeyListener] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (paint g @snake)
      (paint g @apple))

    (actionPerformed [e]
      (update-position snake apple)
      (when (lose? @snake)
        (reset-game snake apple)
        (JOptionPane/showMessageDialog frame "You lose!"))

      (when (win? @snake)
        (reset-game snake apple)
        (JOptionPane/showMessageDialog frame "You win!"))

      (.repaint this))

    (keyPressed [e]
      (update-direction snake
                        (dirs (.getKeyCode e))))

    (getPreferredSize []
      (Dimension. (* (inc width) point-size)
                  (* (inc height) point-size)))

    (keyReleased [e])
    (keyTyped [e])))

(defn game []
  (let [snake (ref (create-snake))
        apple (ref (create-apple))
        frame (JFrame. "Snake")
        panel (game-panel frame snake apple)
        timer (Timer. turn-millis panel)]
    (doto panel
      (.setFocusable true)
      (.addKeyListener panel))

    (doto frame
      (.add panel)
      (.pack)
      (.setVisible true))

    (.start timer)
    [snake apple timer]))
