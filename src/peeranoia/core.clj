(ns peeranoia.core
  (:use compojure.core
        hiccup.core
        hiccup.page-helpers
        hiccup.middleware
        ring.adapter.jetty
        ring.middleware.cookies)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defn home-page
  ([r]
     (let [visits (-> r :cookies (get "value") :value Integer/parseInt inc str)
           referer (get (:headers r) "referer")]
     {:cookies {:value visits,
                :path "/",
                :domain "peeranoia.com" }
      :body
      (html5
       [:head
        [:title "peeranoia"]
        (include-css "/css/site.css")]
       [:body
        [:div#content
         [:h2 "peeranoia"]
         [:div.infoblock "Your IP is: "
          [:label (get (:headers r) "x-real-ip")]]
         [:div.infoblock "Your User-Agent is: "
          [:label (get (:headers r) "user-agent")]]
         [:div.infoblock "Your cookies report that you have been here: "
          [:label (str visits " times")]]
         (when-not (nil? referer)
           [:div.infoblock "You were referred here by: "
            [:label referer]])
         [:p]
         [:div.infoblock "Full headers: "
          [:label (escape-html (:headers r))]]]
        (include-js "/js/g.js")])})))

(defroutes main-routes
  (GET "/" [:as r] (home-page r))
  (route/resources "/")
  (route/not-found "404 - not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-cookies)))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))
