;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns hangman.core
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]))

(defn valid-letter? [c]
  (<= (int \a) (int c) (int \z)))

(defonce available-words
  (with-open [r (jio/reader "words.txt")]
    (->> (line-seq r)
         (filter #(every? valid-letter? %))
         vec)))

(defn rand-word []
  (rand-nth available-words))

(defprotocol Player
  (next-guess [player progress]))

(defn new-progress [word]
  (repeat (count word) \_))

(defn update-progress [progress word guess]
  (map #(if (= %1 guess) guess %2) word progress))

(defn complete? [progress word]
  (= progress (seq word)))

(defn report [begin-progress guess end-progress]
  (println)
  (println "You guessed:" guess)
  (if (= begin-progress end-progress)
    (if (some #{guess} end-progress)
      (println "Sorry, you already guessed:" guess)
      (println "Sorry, the word does not contain:" guess))
    (println "The letter" guess "is in the word!"))
  (println "Progress so far:" (apply str end-progress)))

(defn game
  [word player & {:keys [verbose] :or {verbose false}}]
  (when verbose
    (println "You are guessing a word with" (count word) "letters"))
  (loop [progress (new-progress word), guesses 1]
    (let [guess (next-guess player progress)
          progress' (update-progress progress word guess)]
      (when verbose (report progress guess progress'))
      (if (complete? progress' word)
        guesses
        (recur progress' (inc guesses))))))

;;;; random-player

(defonce letters (mapv char (range (int \a) (inc (int \z)))))

(defn rand-letter []
  (rand-nth letters))

(def random-player
  (reify Player
    (next-guess [_ progress] (rand-letter))))

;;;; random-memory-player

(defrecord ChoicesPlayer [choices]
  Player
  (next-guess [_ progress]
    (let [guess (first @choices)]
      (swap! choices rest)
      guess)))

(defn choices-player [choices]
  (->ChoicesPlayer (atom choices)))

(defn shuffled-player []
  (choices-player (shuffle letters)))

(defn alpha-player []
  (choices-player letters))

(defn freq-player []
  (choices-player (seq "etaoinshrdlcumwfgypbvkjxqz")))

;;;; interactive-player

(defn take-guess []
  (println)
  (print "Enter a letter: ")
  (flush)
  (let [input (.readLine *in*)
        line (str/trim input)]
    (cond
      (str/blank? line) (recur)
      (valid-letter? (first line)) (first line)
      :else (do
              (println "That is not a valid letter!")
              (recur)))))

(def interactive-player
  (reify Player
    (next-guess [_ progress] (take-guess))))

(defn -main [& args]
  (game (rand-word) interactive-player :verbose true))

(comment
  (game "hello" random-player)
  (game "hello" (shuffled-player))
  (game (rand-word) interactive-player :verbose true)
  (game "flask" interactive-player :verbose true)
  (game "hello" (alpha-player))
  (game "hello" (freq-player))
  )