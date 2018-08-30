(ns ifskel.server.db
  (:require
   [environ.core :refer [env]]
   [clojure.tools.namespace.repl :refer [refresh]]
   [suricatta.core :as sc]
   [suricatta.dsl :as dsl]
   [ragtime.jdbc]
   [ragtime.repl]))

(set! *warn-on-reflection* true)

(defn env-dbspec
  "Read the DB Spec from the environment

  * Datasource name (for deployment in wildfly)
  * Configure all parts of the URL: class, protocol, name, user, pass
  "
  []
  (if (or (:db-proto env) (:db-name env))
    ;; dev-mode, use configured db from env
    {:classname (:db-class env)
     :subprotocol (:db-proto env)
     :subname (or (:db-name env)
                  (System/getProperty "db.name"))
     :user (:db-user env)
     :password (:db-pass env)}
    ;; deployed, suricatta wants a datasource
    (let [ctx (javax.naming.InitialContext.)]
      (.lookup (javax.naming.InitialContext.) "PostgresDS"))))



(defn- ragtime-config []
  {:datastore (ragtime.jdbc/sql-database (env-dbspec))
   :migrations (ragtime.jdbc/load-resources
                (str "migrations/" (:db-proto env)))})

(defn run-migrations []
  (ragtime.repl/migrate (ragtime-config)))


(def ^{:dynamic true
       :doc "Suricatta context for with-ctx/suricatta functions"}
  *ctx*
  nil)

(defmacro with-ctx
  "Assures a surricatta context is present

  If *ctx* is already a valid context, it is used,
  otherwise a new dynamic binding is created
  and initialized with a context for +db-spec+"
  [& body]
  `(if *ctx*
     (do ~@body)
     (binding [*ctx* (sc/context (env-dbspec))]
       ~@body)))

(defn qualify-keyword
  "Qualify the keyword in the namespace.

  The keyword is interned in the namespace,
  replacing underscores with -."
  [ns kw]
  (let [^String ns (if (string? ns) ns (name (ns-name ns)))
        ^String kw (if (string? kw) kw (name kw))
        ^String kw (.replace kw \_ \-)]
    (keyword ns kw)))

(defn make-qmap
  "Create a qualification map.

  (make-qmap *ns* [:id :channel_id :channel-name])
  ;;{:id :ifskel.server.db/id,
  ;; :channel_id :ifskel.server.db/channel-id,
  ;; :cname :ifskel.server.db/cname}
  "
  [ns kws]
  (into {} (for [kw kws] [kw (qualify-keyword ns kw)])))

(defn qualifier
  "Return a function to qualify a map using the qmap.

  Keys not in the qmap will be left as-is."
  [qm]
  (fn mapper [m]
    (into {} (for [[k v] (seq m)
                   :let [k (qm k k)
                         v (if (map? v) (mapper v) v)]]
               [k v]))))


;;;
;;; XXX
;;;
;;; demo stuff - delete
;;;

(def +qmap+
  (make-qmap *ns* [:id :todo :created]))

(defn insert-todo [todo]
  (with-ctx
    (sc/fetch-one
     *ctx*
     (-> (dsl/insert-into :todo)
         (dsl/insert-values {:todo todo})
         ;; returning generated ids does not work with h2 or derby
         (dsl/returning :id)))))

(defn list-todos []
  (map (qualifier +qmap+)
       (with-ctx
         (sc/fetch
          *ctx*
          (-> (dsl/select :id :todo :created)
              (dsl/from :todo))))))
