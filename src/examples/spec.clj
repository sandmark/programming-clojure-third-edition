;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]))

(create-ns 'demo.music.release)
(alias 'music 'demo.music.release)

(defn scale-ingredient [ingredient factor]
  (update ingredient :quantity * factor))

;; Specs describing an ingredient
(s/def ::ingredient (s/keys :req [::name ::quantity ::unit]))
(s/def ::name       string?)
(s/def ::quantity   number?)
(s/def ::unit       keyword?)

;; Function spec for scale-ingredient
(s/fdef scale-ingredient
  :args (s/cat :ingredient ::ingredient :factor number?)
  :ret  ::ingredient)

(s/def ::music/id uuid?)
(s/def ::music/artist string?)
(s/def ::music/title string?)
(s/def ::music/date inst?)

(s/def ::music/release
  (s/keys :req [::music/id]
          :opt [::music/artist
                ::music/title
                ::music/date]))

(s/def ::music/release-unqualified
  (s/keys :req-un [::music/id]
          :opt-un [::music/artist
                   ::music/title
                   ::music/date]))

(s/def ::rand-args (s/cat :n (s/? number?)))

(s/def ::rand-ret double?)

(s/def ::rand-fn
  (fn [{:keys [args ret]}]
    (let [n (or (:n args) 1)]
      (cond (zero? n) (zero? ret)
            (pos? n) (and (>= ret 0) (< ret n))
            (neg? n) (and (<= ret 0) (> ret n))))))

(s/fdef clojure.core/rand
  :args ::rand-args
  :ret  ::rand-ret
  :fn   ::rand-fn)

(defn opposite [pred]
  (comp not pred))

(s/def ::pred
  (s/fspec :args (s/cat :x any?)
           :ret  boolean?))

(s/fdef opposite
  :args (s/cat :pred ::pred)
  :ret ::pred)

(s/fdef clojure.core/symbol
  :args (s/cat :ns (s/? string?) :name string?)
  :ret symbol?
  :fn (fn [{:keys [args ret]}]
        (and (= (name ret) (:name args))
          (= (namespace ret) (:ns args)))))
