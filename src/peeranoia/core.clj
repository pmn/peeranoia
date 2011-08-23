(ns peeranoia.core
  (:use [compojure.core :only [defroutes GET]]
        [hiccup.core :only [escape-html html]]
        [hiccup.page-helpers :only [include-css include-js html5]]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware.cookies :only [wrap-cookies]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defn- render-page
  [{:keys [visits referer ip user-agent headers]}]
  (html5
   [:head
    [:title "peeranoia"]
    (include-css "/css/site.css")]
   [:body
    [:div#content
     [:h2 "peeranoia"]
     [:div.infoblock "Your IP is: "
      [:span ip]]
     [:div.infoblock "Your User-Agent is: "
      [:span user-agent]]
     [:div.infoblock "Your cookies report that you have been here: "
      [:span (str visits " times")]]
     (when-not (nil? referer)
       [:div.infoblock "You were referred here by: "
        [:span referer]])
     [:p]
     [:div.infoblock "Full headers: "
      [:span (escape-html headers)]]]
    (include-js "/js/g.js")]))

(defn home-page
  ([r]
     (let [visits (-> r :cookies (get "value") :value Integer/parseInt inc str)
           referer (get (:headers r) "referer")
           ip (get (:headers r) "x-real-ip")
           user-agent (get (:headers r) "user-agent")
           headers (:headers r)]
     {:cookies {:value visits,
                :path "/",
                :domain "peeranoia.com" }
      :body
      (render-page {:visits visits :referer referer :ip ip :user-agent user-agent :headers headers})})))


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
