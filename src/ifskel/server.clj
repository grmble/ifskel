(ns ifskel.server
  "Main Server Namespace for deployment"
  (:require
   [cheshire.core :as json]
   [clojure.tools.logging :as log]
   [clojure.tools.namespace.repl :refer [refresh]]
   [clojure.string :as string]
   [compojure.core :refer :all]
   [compojure.route :as route]
   [immutant.web :as web]
   [ifskel.server.db :as db]
   [ring.middleware.resource]
   [ring.middleware.content-type]
   [ring.middleware.not-modified]
   ;; [taoensso.tufte :as tufte :refer (defnp p profiled profile)]
   )
  (:gen-class))

;;
;; for profiling output
;; (tufte/add-basic-print-handler!)
;; (tufte/add-handler! :global tufte-log-handler)
;;
#_
(defn tufte-log-handler [{:keys [ns-str level pstats]}]
  ;; level: [0, 5]
  (let [logger (clojure.tools.logging.impl/get-logger log/*logger-factory* ns-str)]
    (when (log/enabled? :debug logger)
      (log/log* logger :debug nil (tufte/format-pstats pstats)))))

(defroutes router
  ;; redirect - resource-based middleware does not do this by default
  (GET "/" []
       {:status 302
        :headers {"Location" "index.html"}
        :body ""})
  (GET "/todos" [ ]
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (json/generate-string (db/list-todos))})
  (route/not-found "<h1>Page not found</h1>")
  )

(def ^{:doc "Main application.

The compojure router and a file server on the directory public
"}
  app (-> #'router
          (ring.middleware.resource/wrap-resource "public")
          ;; XXX not much faster for development
          (ring.middleware.content-type/wrap-content-type)
          (ring.middleware.not-modified/wrap-not-modified)
          ))

(defn -main
  "Run the web server


  This calls (web/run app).
  In the repl, you may want to use (web/run-dcm app).
  "
  [& args]

  (log/info "Serving the application via web/run")
  (web/run app))
