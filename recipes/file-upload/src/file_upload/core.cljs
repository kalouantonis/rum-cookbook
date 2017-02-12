(ns file-upload.core
  (:require [ajax.core :refer [POST]]
            [rum.core :as rum]))

(enable-console-print!)

;; Make FileList usable with (seq ...)
(extend-type js/FileList
  ISeqable
  (-seq [files] (array-seq files 0)))

(defn- wrap-native-event
  "Wrapper for react event handler. Calls `f` with
  the native DOM node rather than the react SyntheticEvent."
  [f]
  (fn [e]
    (f (.-nativeEvent e))))

(defn- wrap-prevent-default
  "Wrapper for react event handler. Always prevent default
  on the received event, even if an exception is thrown when
  calling `f`."
  [f]
  (fn [e]
    (try (f e)
         (finally
           (.preventDefault e)))))

(defn upload-song! [file]
  (let [form-data (doto (js/FormData.)
                    (.append "file" file))]
    (POST "/api/files" {:body form-data
                        :handler #(println "Uploaded file" file)
                        :error-handler #(println "Failed to upload file:" %)})))

(rum/defc file-form [files]
  [:form.form-inline {:enc-type "multipart/form-data"
                      :method "POST"
                      :on-submit (wrap-prevent-default
                                  (fn [_]
                                    (doseq [file @files]
                                      (upload-song! (:file file)))))}
   [:div.form-group
    [:input {:type "file", :multiple true
             :on-change (wrap-native-event
                         (fn [e]
                           (swap! files into (set (-> e .-target .-files)))))}]]
   [:button.btn.btn-default {:type :submit}
    "Upload "[:i.fa.fa-upload]]])

(rum/defc file-list [files]
  [:table.table
   [:thead
    [:tr
     [:th "Name"]
     [:th "Size"]
     [:th]]]
   [:tbody
    (for [file @files]
      [:tr {:key (hash file)}
       [:td (.-name file)]
       [:td (.-size file)]
       [:td [:button.btn.btn-default
             {:on-click #(swap! files disj file)}
             "Remove"]]])]])

(rum/defcs upload-form < (rum/local #{} ::files)
  [state]
  [:#upload
   [:.row
    [:.col-md-12
     (file-list (::files state))]]
   [:.row
    [:.col-md-12
     (file-form (::files state))]]])

(defn mount! []
  (rum/mount (upload-form)
             (js/document.getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (mount!))

;; First init
(mount!)
