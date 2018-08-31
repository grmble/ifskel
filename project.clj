(defproject ifskel "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/grmble/ifskel"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 ;;
                 ;; BACKEND
                 ;;
                 ;;
                 ;; immutant: webserver/cache/messaging
                 ;; tools.logging: for logging
                 ;; tools.namespace: refresh!!!
                 ;; cheshire: handling json
                 ;; environ/lein-environ for configuration
                 ;; data.json: to/from json
                 ;; tufte: performance profiling
                 ;; ring-devel: for run-dmc - reloading, stack traces
                 ;; compojure: server side routing
                 ;; suricatta: sql backed by jooq
                 ;; ragtime: migrations
                 ;; embedded postgresql for dev/testing
                 ;;
                 ;;
                 ;; FRONTEND
                 ;;
                 ;; figwheel-main:
                 ;; clojurescript re-compilation, hot-reloading
                 ;;
                 ;; figwheel-main
                 ;; reagent
                 ;; metosin/compojure-api: SWAGGER! (prismatic) schema!
                 ;;
                 ;;
                 ;; RECOMMENDED Plugins
                 ;;
                 ;; Recommended for :user profile (.lein/profiles.clj)
                 ;;
                 ;; ancient
                 ;; kibit
                 ;; eastwood
                 ;; cljfmt
                 [org.immutant/immutant "2.1.10"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [cheshire "5.8.0"]
                 ;; [com.taoensso/tufte "2.0.1"]
                 [ring/ring-core "1.7.0-RC1"]
                 [compojure "1.6.1"]
                 [funcool/suricatta "1.3.1"]
                 [ragtime "0.7.2"]
                 [environ "1.1.0"]
                 ]

  :aliases
  {;; trampoline does not work on windows
   ;; "fig" ["trampoline" "run" "-m" "figwheel.main"]
   "fig" ["run" "-m" "figwheel.main" "-b" "dev" "-r"]
   "fig:min" ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
   "js:cp" ["shell" "cp" "target/public/cljs-out/dev-main.js" "cljs-resources/public/cljs-out/"]
   "war:cp" ["with-profile" "production,immutant" "immutant" "war"]
   "war:all" ["do" ["clean"] ["fig:min"] ["js:cp"] ["war:cp"]]
   }

  :main ^:skip-aot ifskel.server
  :target-path "target/%s"

  :plugins [[lein-immutant "2.1.0"]]

  :profiles {:dev
             {:env {:db-class "org.postgresql.Driver"
                    :db-proto "postgresql"
                    ;; :db-name is set by embedded db startup
                    ;; :db-user and :db-pass are in name part
                    }

              :main ifskel.dev

              :dependencies [[ring/ring-devel "1.7.0-RC1"]
                             [org.postgresql/postgresql "42.2.5"]
                             [ru.yandex.qatools.embed/postgresql-embedded "2.10-SNAPSHOT"]


                             [org.clojure/clojurescript "1.10.339"]
                             [com.bhauman/figwheel-main "0.1.8"]


                             ;; won't work without trampoline
                             ;; trampoline doesn't work on windows
                             ;; [com.bhauman/rebel-readline-cljs "0.1.4"]
                             [reagent "0.8.1"]
                             [cljs-http "0.1.45"]

                             ;; devcards
                             [devcards "0.2.5"]
                             ;; need these reacts for reagent 0.8.1
                             [cljsjs/react-dom "16.3.2-0"]
                             [cljsjs/react "16.3.2-0"]
                             ]
              :plugins [[lein-environ "1.1.0"]
                        [lein-shell "0.5.0"]]
              :source-paths ["dev"]
              ;; so cljs compilation results are picked up
              :resource-paths ["target"]
              :clean-targets ^{:protect false} ["target"]}

             :production
             {
              ;; so cljs compilation results are picked up
              :resource-paths ["cljs-resources"]
              }

             :uberjar {:aot :all}
             }

  )
