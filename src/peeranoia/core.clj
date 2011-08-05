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
       [:title "loaded"]
       (include-css "/css/site.css")]
      [:body
       [:h1 "peeranoia"]
       [:div (escape-html (str r))]
       [:ul
        [:li (:remote-addr r)]
        ]])))

(defroutes main-routes
  (GET "/" [:as r] (home-page r))
  (GET "/test" [] "<h1>testinggg</h1>")
  (route/resources "/")
  (route/not-found "404 - not found"))

(def app
  (handler/site main-routes))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))
