(ns peeranoia.core
  (:use compojure.core
        hiccup.core
        hiccup.page-helpers
        hiccup.middleware
        ring.adapter.jetty)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defn home-page
  ([r]
     (html5
      [:head
       [:title "peeranoia"]
       (include-css "/css/site.css")]
      [:body
       [:h1 "peeranoia"]
       [:div (escape-html (str r))]
       [:ul
        [:li (str "remote-addr:" (:remote-addr r))]
        [:li (str "x-forwarded-for:" (:x-forwarded-for r))]
        [:li (str "cookies:" (:cookies r))]
        [:li (str "user-agent:" (get (:headers r) "user-agent"))]
        ]])))

(defroutes main-routes
  (GET "/" [:as r] (home-page r))
  (route/resources "/")
  (route/not-found "404 - not found"))

(def app
  (handler/site main-routes))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))
