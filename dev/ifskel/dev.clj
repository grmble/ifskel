(ns ifskel.dev
  "Main namespace for dev build

  * Run it with `lein run`
  * In a repl, `(start)` to start, `(stop)` to stop it
  * Reloading in the browser will re-compile if anything changed
  * Or you can `(refresh)` in the repl

  For embedded PG under windows, stopping does not work.
  A patched version is at https://github.com/grmble/postgres-embedded
  "
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.repl]
   [clojure.tools.namespace.repl :refer [refresh]]
   [ifskel.server]
   [ifskel.server.db]
   [immutant.web :as web]
   )
  (:import
   (ru.yandex.qatools.embed.postgresql EmbeddedPostgres)
   (ru.yandex.qatools.embed.postgresql.config PostgresConfig)))

(def ^{:tag String
       :doc "location for embedded postgres install"}
  +embedded-pg-cache+ "tmp/embedded-pg")

(def ^{:tag String
       :doc "location for embedded postgres data"}
  +embedded-pg-data+ "tmp/embedded-pg-data")

(defonce ^{:doc "instance for embedded pg when running"}
  +embedded-pg+
  (EmbeddedPostgres. +embedded-pg-data+))

(defn- embedded-pg-runtime-config []
  (->> +embedded-pg-cache+
       (io/file)
       (.toPath)
       (EmbeddedPostgres/cachedRuntimeConfig)))

(defn start-embedded-pg []
  (let [uri (.start +embedded-pg+ (embedded-pg-runtime-config))
        pre "jdbc:postgresql:"]
    (if (string/starts-with? uri pre)
      (do
        (System/setProperty "db.name" (subs uri (.length pre)))
        uri)
      (throw (ex-info "unexpected uri prefix" {:pre pre :uri uri})))))

(defn stop-embedded-pg []
  (.stop +embedded-pg+))


(defn start
  "Start all the things"
  []
  (start-embedded-pg)
  (ifskel.server.db/run-migrations)
  (web/run-dmc ifskel.server/app))

(defn stop
  "... and stop it again"
  []
  (web/stop)
  (stop-embedded-pg))
