(ns live.session
  (:require [overtone.core :as o]))

(defn boot!
  "Start SuperCollider with stereo output."
  []
  (when-not (o/server-connected?)
    (o/boot-external-server nil {:max-input-bus 0 :max-output-bus 2}))
  (o/wait-until-mixer-booted)
  :ready)

(o/defsynth tone [freq 440 amp 0.12]
  (let [env (o/env-gen (o/perc 0.01 0.4) :action o/FREE)
        signal (* amp env (o/sin-osc freq))]
    (o/out 0 (o/pan2 signal))))

(o/defsynth kick [amp 0.36]
  (let [env (o/env-gen (o/perc 0.002 0.22) :action o/FREE)
        pitch (o/x-line 115 44 0.06)
        signal (* amp env (o/sin-osc pitch))]
    (o/out 0 (o/pan2 signal))))

(o/defsynth hat [amp 0.12]
  (let [env (o/env-gen (o/perc 0.002 0.055) :action o/FREE)
        signal (* amp env (o/hpf (o/white-noise) 7000))]
    (o/out 0 (o/pan2 signal))))

(o/defsynth bass [freq 55 amp 0.19]
  (let [env (o/env-gen (o/perc 0.008 0.28) :action o/FREE)
        signal (* amp env (o/lpf (o/saw freq) 650))]
    (o/out 0 (o/pan2 signal))))

(defonce clock (o/metronome 110))
(defonce ^:private active-run (atom nil))

(defn play-step [beat step]
  (let [notes [45 nil 45 48 43 nil 43 40]]
    (o/at (clock beat)
      (when (even? step) (kick))
      (when-let [note (notes (mod step (count notes)))]
        (bass (o/midi->hz note))))
    (o/at (clock (+ beat 0.5)) (hat))))

(defn- play-loop [run beat step]
  (locking active-run
    (when (identical? run @active-run)
      (play-step beat step)
      (let [next-beat (inc beat)]
        (o/apply-by (clock next-beat) #'play-loop [run next-beat (inc step)])))))

(defn start!
  "Start the groove once, with a little time for the first beat to be scheduled."
  []
  (locking active-run
    (when-not @active-run
      (when-not (o/server-connected?)
        (throw (ex-info "Run (boot!) before starting the groove." {})))
      (let [run (Object.)]
        (reset! active-run run)
        (play-loop run (inc (clock)) 0)
        :playing))))

(defn stop!
  "Stop the groove and clear notes already queued on SuperCollider."
  []
  (locking active-run
    (reset! active-run nil)
    (when (o/server-connected?)
      (o/snd "/clearSched")
      (o/stop))
    :stopped))

(defn bpm!
  "Read or change the tempo in beats per minute."
  ([] (o/metro-bpm clock))
  ([bpm]
   (when-not (and (number? bpm) (Double/isFinite (double bpm)) (pos? bpm))
     (throw (ex-info "BPM must be a positive, finite number." {:bpm bpm})))
   (o/metro-bpm clock bpm)))

(defn shutdown!
  "Stop playback and shut down SuperCollider."
  []
  (locking active-run
    (stop!)
    (when (o/server-connected?)
      (o/kill-server))))

(comment
  (boot!)
  (tone)
  (start!)
  (bpm! 124)
  (o/volume 0.5)
  (stop!)
  (shutdown!))
