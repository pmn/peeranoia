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
       [:div#content
       [:h1 "peeranoia"]
        [:div.infoblock "Your IP is: "
         [:label (get (:headers r) "x-real-ip")]]
        [:div.infoblock "Your User-Agent is: "
         [:label (get (:headers r) "user-agent")]]
        [:div.infoblock "Cookies: "
         [:label (:cookies r)]]]])))

(defroutes main-routes
  (GET "/" [:as r] (home-page r))
  (route/resources "/")
  (route/not-found "404 - not found"))

(def app
  (handler/site main-routes))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))
