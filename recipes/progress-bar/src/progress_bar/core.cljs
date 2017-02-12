(ns progress-bar.core
  (:require [rum.core :as rum]))

(enable-console-print!)

(defn clamp
  "Restrict value `x` between `min` and `max`."
  [x min max]
  (cond
    (< x min) min
    (> x max) max
    :else x))

(rum/defc progress-bar < rum/static
  [value]
  [:.progress
   [:.progress-bar {:role "progressbar"
                    :aria-valuenow value
                    :aria-valuemin 0
                    :aria-valuemax 100
                    :style {:width (str value "%")}}
    [:span.sr-only (str value "% Complete")]]])

(rum/defcs progress-view < (rum/local 25 ::value)
  [state]
  (let [value (::value state)]
    [:.row
     [:.col-md-6 (progress-bar @value)]
     [:.col-md-6
      [:.button-group
       [:button.btn.btn-default
        {:on-click (fn [_]
                     (swap! value #(clamp (- % 5) 0 100)))}
        "-"]
       [:button.btn.btn-default
        {:on-click (fn [_]
                     (swap! value #(clamp (+ % 5) 0 100)))}
        "+"]]]]))

(defn mount! []
  (rum/mount (progress-view)
             (js/document.getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (mount!))

;; Initial app start
(mount!)
