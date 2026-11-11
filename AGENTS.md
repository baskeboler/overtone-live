# Repository Guidelines

## Project Structure & Module Organization

- `src/live/session.clj` contains the `live.session` namespace: SuperCollider lifecycle, synths, tempo, and groove scheduling.
- `dev/user.clj` provides REPL helpers; `dev/row_row_row_your_boat.clj` contains a Leipzig composition example.
- `deps.edn` declares dependencies, source paths (`src` and `dev`), and the `:nrepl` alias.
- `mise.toml` configures Java, Clojure, and the REPL task. `README.md` documents the interactive audio workflow.

There are currently no dedicated test or asset directories.

## Build, Test, and Development Commands

- `mise install`: install the configured development runtimes.
- `pw-jack clj -M`: start a terminal REPL through PipeWire's JACK compatibility layer.
- `pw-jack clojure -M:nrepl`: start an editor-connectable nREPL bound to localhost.
- `mise run repl`: run the configured nREPL task; this task does not add `pw-jack`.

In the REPL, require `[live.session :as live]`, then evaluate `(live/boot!)`, `(live/tone)`, and `(live/start!)` individually. Use `(live/bpm! 124)` to change tempo, `(live/stop!)` to stop playback, and `(live/shutdown!)` to release the server. No packaging or automated test command is configured.

## Coding Style & Naming Conventions

Follow the existing Clojure style: two-space body indentation, aligned bindings, and kebab-case names. Namespace segments map to directories; hyphens map to underscores in filenames. Use `!` for state-changing lifecycle functions, as in `start!`. Keep interactive examples in `comment` forms and preserve safe re-evaluation during live coding. No formatter or lint task is configured.

## Testing Guidelines

Validate audio changes interactively: boot, play a tone, start the groove, change tempo, stop, restart, and shut down. Re-evaluate changed definitions during playback to check live-coding behavior. Report audible results separately from namespace-loading checks.

No automated framework or coverage threshold is configured. For future pure-logic tests, prefer `clojure.test`, `test/live/*_test.clj`, and namespaces such as `live.session-test`; add an explicit test path and runner before documenting a test command.

## Commit & Pull Request Guidelines

Git history currently contains only `initial commit`, so no established message convention exists. Use concise, imperative subjects describing one focused change. In pull requests, explain the behavior changed, include reproduction or REPL steps, and state verification performed. Link relevant issues and include an audio sample when it helps reviewers assess sound changes. Keep unrelated editor files and local REPL history out of commits.
