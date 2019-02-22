;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---
(ns examples.protocols
  (:import (java.net Socket URL)
           (java.io File FileInputStream FileOutputStream
                    InputStream InputStreamReader
                    BufferedReader BufferedWriter
                    OutputStream OutputStreamWriter)))

(defn gulp [src]
  (let [sb (StringBuilder.)]
    (with-open [reader (make-reader src)]
      (loop [c (.read reader)]
        (if (neg? c)
          (str sb)
          (do
            (.append sb (char c))
            (recur (.read reader))))))))


(defn expectorate [dst content]
  (with-open [writer (make-writer dst)]
    (.write writer (str content))))


(defn make-reader [src]
  (-> (condp = (type src)
          java.io.InputStream src
          java.lang.String (FileInputStream. src)
          java.io.File (FileInputStream. src)
          java.net.Socket (.getInputStream src)
          java.net.URL (if (= "file" (.getProtocol src))
                         (-> src .getPath FileInputStream.)
                         (.openStream src)))
      InputStreamReader.
      BufferedReader.))


(defn make-writer [dst]
  (-> (condp = (type dst)
          java.io.OutputStream dst
          java.io.File (FileOutputStream. dst)
          java.lang.String (FileOutputStream. dst)
          java.net.Socket (.getOutputStream dst)
          java.net.URL (if (= "file" (.getProtocol dst))
                         (-> dst .getPath FileOutputStream.)
                         (throw (IllegalArgumentException.
                                 "Can't write to non-file URL"))))
      OutputStreamWriter.
      BufferedWriter.))


(extend InputStream
  IOFactory
  {:make-reader (fn [src]
                  (-> src InputStreamReader. BufferedReader.))
   :make-writer (fn [dst]
                  (throw (IllegalArgumentException.
                          "Can't open as an InputStream.")))})


(extend OutputStream
  IOFactory
  {:make-reader (fn [src]
                  (throw
                   (IllegalArgumentException.
                    "Can't open as an OutputStream.")))
   :make-writer (fn [dst]
                  (-> dst OutputStreamWriter. BufferedWriter.))})

(extend-type File
  IOFactory
  (make-reader [src]
    (make-reader (FileInputStream. src)))
  (make-writer [dst]
    (make-writer (FileOutputStream. dst))))

(extend-protocol IOFactory
  Socket
  (make-reader [src]
    (make-reader (.getInputStream src)))

  (make-writer [dst]
    (make-writer (.getOutputStream dst)))

  URL
  (make-reader [src]
    (make-reader
     (if (= "file" (.getProtocol src))
       (-> src .getPath FileInputStream.)
       (.openStream src))))

  (make-writer [dst]
    (make-writer
     (if (= "file" (.getProtocol dst))
       (-> dst .getPath FileInputStream.)
       (throw (IllegalArgumentException.
               "Can't write to non-file URL"))))))

(defprotocol MidiNote
  (to-msec [this tempo])
  (key-number [this])
  (play [this tempo midi-channel]))

(import 'javax.sound.midi.MidiSystem)
(extend-type Note
  MidiNote
  (to-msec [this tempo]
    (let [duration-to-bpm {1 240, 1/2 120, 1/4 60, 1/8 30, 1/16 15}]
      (* 1000 (/ (duration-to-bpm (:duration this))
                 tempo))))

  (key-number [this]
    (let [scale {:C 0,  :C# 1, :Db 1,  :D 2,
                 :D# 3, :Eb 3, :E 4,   :F 5,
                 :F# 6, :Gb 6, :G 7,   :G# 8,
                 :Ab 8, :A 9,  :A# 10, :Bb 10,
                 :B 11}]
      (+ (* 12 (inc (:octave this)))
         (scale (:pitch this)))))
  
  (play [this tempo midi-channel]
    (let [velocity (or (:velocity this) 64)]
      (.noteOn midi-channel (key-number this) velocity)
      (Thread/sleep (to-msec this tempo)))))
  
(defn perform [notes & {:keys [tempo] :or {tempo 120}}]
  (with-open [synth (doto (MidiSystem/getSynthesizer) .open)]
    (let [channel (aget (.getChannels synth) 0)]
      (doseq [note notes]
        (play note tempo channel)))))
                                        