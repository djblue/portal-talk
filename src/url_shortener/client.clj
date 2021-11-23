(ns url-shortener.client
  (:require [org.httpkit.client :as http]))

(defn get-url [url]
  (let [response @(http/get url {:follow-redirects false})]
    (case (:status response)
      307 (get-in response [:headers :location])
      404 nil)))

(defn shorten-url [url]
  (let [response
        @(http/post
          "http://localhost:3000"
          {:body url})]
    (case (:status response)
      200 (slurp (:body response))
      400 (throw (ex-info "Invalid URL"
                          {:url      url
                           :response response})))))
