(ns ivankytype.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ivankytype.view.main :as main]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]])
  (:import (java.lang Integer)))

(def config {:min-count 10
             :port 3000})

(defroutes app-routes
  (wrap-routes
   (routes
       (GET "/" [size mode] (main/index (if size (Integer/parseInt size) (:min-count config)) mode))
       (route/resources "/"))
   wrap-params))

(defn -main [& args]
  (run-jetty #'app-routes {:port (:port config)}))

(comment
  (future (-main))
  )
