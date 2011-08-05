(ns peeranoia.core
  (:use compojure.core
        hiccup.core
        hiccup.page-helpers
        hiccup.middleware
        ring.adapter.jetty)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defn home-page
  ([]
     (html5
      [:head
       [:title "loaded"]
       (include-css "/css/site.css")]
      [:body
       [:h1 "peeranoia"]])))

(defroutes main-routes
  (GET "/" [] (home-page))
  (route/resources "/")
  (route/not-found "404 - not found"))

(def app
  (-> main-routes
      (wrap-base-url)))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))
