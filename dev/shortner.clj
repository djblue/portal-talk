(ns shortner
  (:require [url-shortener.client :as client]
            [url-shortener.server :as server]))

;; 0. API Setup

(require '[portal.api :as p])
(add-tap #'portal.api/submit)
(reset! server/db {})


;; 1. Shortner API

(def url "https://github.com/djblue/portal")

(def short-url (client/shorten-url url))

(tap> short-url)

(tap> (client/get-url short-url))


;; 2. API BUG

(def short-url-2 (client/shorten-url url))

(tap> short-url-2)

(tap> (client/get-url short-url-2))

(doto (= short-url short-url-2) tap>)

(tap> @server/db)


;; 3. Debugging

(require '[url-shortener.console :refer [toggle]])
(toggle) ; Enable cheat mode

;; I don't like inverse chronological right now
(portal.api/register! #'reverse)
