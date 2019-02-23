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
  (:refer examples.import-static :refer :all))

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
  (map #(* point-size %) [pt1 pt2 1 1]))
