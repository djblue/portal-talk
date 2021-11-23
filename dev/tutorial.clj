(ns tutorial
  "This tutorial will cover the basics of the Portal API/UI"
  (:require [clojure.repl :refer [doc]]
            [portal.api :as p]
            [user]))

;; Questions
;;
;; If you have any questions, please note them down here and ask them during the
;; question section:


;; 0. Setup API

(def portal (p/open))
(add-tap #'p/submit)

;; 1. tap>

(doc tap>) ; for more info


;; 2. Simple data and the tap list
;; - List is inverse chronological order

(tap> 42)
(tap> :abc)
(tap> [1 2 3])
(tap> {:x 0 :y 0})
(tap> {:x 0 :y 1})


;; 3. Selection & Viewers
;; - Single click to select
;; - bottom-left to change viewer
;; - e to expand / collapse a value
;; - cmd + click to select multiple values

(tap> [{:color :blue} {:color :red}]) ;; looks like a table

(-> @portal) ;; get selected value


;; 4. Commands
;; - Select any value
;; - Press bottom-right command button
;; - Choose command (keys)


;; 5. History
;; - Top-left Arrows will manipulate history

(p/clear) ;; clear all history


;; 6. Shortcuts
;; - Keyboard shortcuts are listed in the command prompt
