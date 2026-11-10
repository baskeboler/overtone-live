# Overtone live coding

Open a terminal in this folder and start the Clojure REPL through PipeWire.

```sh
pw-jack clj -M
```

Then evaluate these forms one at a time. `tone` plays a short sound and `start!` starts the groove.

```clojure
(require '[live.session :as live])
(live/boot!)
(live/tone)
(live/start!)
(live/bpm! 124)
(live/stop!)
(live/shutdown!)
```

After `stop!`, call `start!` to play again. After `shutdown!`, call `boot!` to restart SuperCollider or press Ctrl-D to leave the terminal REPL.

## Edit while it plays

From the project folder, start the editor REPL with `pw-jack clojure -M:nrepl` and connect your editor's Clojure extension to the printed localhost port. Open `src/live/session.clj` and load the file into the REPL. The `comment` block at the bottom contains forms to evaluate individually.

While the groove plays, edit the notes or drum triggers in `play-step` and evaluate that definition again. The next unscheduled beat uses the new definition. You can also re-evaluate a synth definition to change how future notes sound. In a terminal REPL, save your changes and run `(load-file "src/live/session.clj")` to reload the file.

If you hear silence, run `wpctl status` in your desktop terminal and check which audio output is selected. You can set the Overtone mixer level with `(overtone.core/volume 0.5)`.

[Overtone quick start](https://github.com/overtone/overtone#quick-start) · [Linux audio](https://github.com/overtone/overtone/wiki/Linux-Audio-Primer) · [Live coding](https://github.com/overtone/overtone/wiki/Live-coding)
