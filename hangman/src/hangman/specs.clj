;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns hangman.specs
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [hangman.core :as h :refer
             [letters valid-letter? Player
              random-player shuffled-player alpha-player freq-player]]))

;; letters and words

(s/def ::letter (set letters))

(s/def ::word
  (s/with-gen
    (s/and string?
           #(pos? (count %))
           #(every? valid-letter? (seq %)))
    #(gen/fmap
       (fn [letters] (apply str letters))
       (s/gen (s/coll-of ::letter :min-count 1)))))

;; progress functions

(s/def ::progress-letter
  (conj (set letters) \_))

(s/def ::progress
  (s/coll-of ::progress-letter :min-count 1))

(defn- letters-left
  [progress]
  (->> progress (keep #{\_}) count))

(s/fdef hangman.core/new-progress
  :args (s/cat :word ::word)
  :ret ::progress
  :fn (fn [{:keys [args ret]}]
        (= (count (:word args)) (count ret) (letters-left ret))))

(s/fdef hangman.core/update-progress
  :args (s/cat :progress ::progress :word ::word :guess ::letter)
  :ret ::progress
  :fn (fn [{:keys [args ret]}]
        (>= (-> args :progress letters-left)
          (-> ret letters-left))))

(s/fdef hangman.core/complete?
  :args (s/cat :progress ::progress :word ::word)
  :ret boolean?)

;; game

(defn player? [p]
  (satisfies? Player p))

(s/def ::player
  (s/with-gen player?
    #(s/gen #{random-player
              shuffled-player
              alpha-player
              freq-player})))

(s/def ::verbose (s/with-gen boolean? #(s/gen false?)))
(s/def ::score pos-int?)

(s/fdef hangman.core/game
  :args (s/cat :word ::word
               :player ::player
               :opts (s/keys* :opt-un [::verbose]))
  :ret ::score)

(defn run-gen []
  (stest/summarize-results
    (stest/check (stest/enumerate-namespace 'hangman.core))))

(comment
  (stest/instrument (stest/enumerate-namespace 'hangman.core))
  (stest/summarize-results (stest/check 'hangman.core/new-progress))
  (stest/check 'hangman.core/update-progress)
  (stest/check 'hangman.core/complete?)
  (stest/summarize-results (stest/check 'hangman.core/game))
  (run-gen)
  (-> 'hangman.core stest/enumerate-namespace stest/check stest/summarize-results)
  )
