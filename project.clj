(defproject peeranoia "0.0.0.1"
  :description "peeranoia.com"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [compojure "0.6.2"]
                 [hiccup "0.3.5"]
                 [ring/ring-core "0.3.8"]
                 [ring/ring-jetty-adapter "0.3.8"]]
  :dev-dependencies [[lein-ring "0.4.5"]]
  :exclusions [org.mortbay.jetty/servlet-api]
  :ring {:handler peeranoia.core/app})
