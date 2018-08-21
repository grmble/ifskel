(ns ifskel.client
  "Main Frontend Namespace"
  (:require
   [cljs.core.async :refer [<!]]
   [cljs-http.client :as http]
   [reagent.core :as r]
   )
  (:require-macros
   [cljs.core.async.macros :refer [go]]
   ))

(defonce app-state (r/atom {:counter 0
                            :hello ""
                            :todos []}))

(defn get-todos []
  (go (let [response (<! (http/get "/todos"))]
        (js/console.log response)
        (when (= 200 (:status response))
          (swap! app-state assoc :todos (:body response))
          ))))

(defn counter-component [counter]
   [:div
    [:h3 "Counter"]
    [:p "The counter value is: " counter]
    [:button {:on-click #(swap! app-state update :counter inc)} "Inc"]
    [:button {:on-click #(swap! app-state update :counter dec)} "Dec"]
    ])

(defn hello-component [world]
  [:div
   [:h3 "Hello"]
   [:p "Hello, " @world]
   [:input {:value @world
            :on-change
            #(let [v (-> % .-target .-value)]
               (reset! world v))}]])

(defn todos-component [todos]
  [:div
   [:h3 "Todos"]
   [:ul
    (for [todo @todos]
      [:li
       (:ifskel.db/todo todo)])]
   [:button {:on-click #(get-todos)} "Get Todos"]
   ])


(defn master-component []
  [:div
   [hello-component (r/cursor app-state [:hello])]
   [counter-component (:counter @app-state)]
   [todos-component (r/cursor app-state [:todos])]
   ])

(r/render
 [master-component]
 (js/document.getElementById "app"))
