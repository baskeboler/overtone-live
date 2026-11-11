(ns user
  (:require [live.session :as live]))

(comment
  (live/boot!)
  (live/tone)
  (live/start!)
  (live/stop!)
  (live/tone)
  (live/shutdown!))
