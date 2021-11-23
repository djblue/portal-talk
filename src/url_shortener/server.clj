(ns url-shortener.server
  (:require [nano-id.core :as nano]
            [org.httpkit.server :as http]
            [url-shortener.console :refer [log]])
  (:import (java.net URL)
           (java.util Date)
           (org.apache.commons.validator.routines UrlValidator)))

(def config {:protocol "http"
             :host     "localhost"
             :port     3000})

(defonce db (atom {}))

(defn get-url [request]
  (if-let [url (get-in @db [(subs (:uri request) 1) :url])]
    {:status 307
     :headers {"location" url}}
    {:status 404}))

(defn create-record [url]
  (let [id (nano/nano-id 10)]
    {:id           id
     :url          url
     :created      (Date.)
     :generate-url (URL.
                    (str
                     (:protocol config) "://" (:host config) ":" (:port config) "/" id))}))

(defn exists? [db url] (contains? db (str url)))

(defn add-url [db url]
  (log
   (if (log (exists? db url))
     db
     (let [record (create-record url)]
       (assoc db
              url          record
              (:id record) record)))))

(defn url-shortner [url]
  (log @db)
  (-> db
      (swap! add-url url)
      (get url)
      :generate-url))

(defn valid-url? [string]
  (.isValid
   (UrlValidator. UrlValidator/ALLOW_LOCAL_URLS)
   string))

(defn post-url [request]
  (let [url (slurp (:body request))]
    (log url)
    (if-not (valid-url? url)
      {:status 400 :body "Invalid URL"}
      {:status 200 :body (str (url-shortner (URL. url)))})))

(defn handler [request]
  (log request)
  (log
   (case (:request-method request)
     :get  (get-url request)
     :post (post-url request)
     {:status 404})))

(defonce server (atom nil))

(defn start []
  (println "Starting server on http://localhost:3000")
  (http/run-server #'handler
                   {:port (:port config)
                    :legacy-return-value? false}))

(defn stop []
  (some-> server deref http/server-stop!))
