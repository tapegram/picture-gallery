(ns picture-gallery.routes.services.auth
  (:require [picture-gallery.db.core :as db]
            [ring.util.http-response :as response]
            [buddy.hashers :as hashers]
            [clojure.tools.logging :as log]))

(defn register! [{:keys [session]} user]
  (try
    (db/create_user!
     (-> user
         (dissoc :pass-confirm)
         (update :pass hashers/encrypt)))
    (-> {:result :ok}
        (response/ok)
        (assoc :session (assoc session :identity (:id user))))
    (catch Exception e
      (log/error e)
      (response/internal-server-error
       {:result :error
        :message "server error occurred while adding the user"}))))
