(defn act [actor role]
  (let [[name roles] actor]
    (println name "playing role of" role)
    [name (conj roles role)]))

(defn sendMessageInTXN [actor role shouldFail]
  (try
    (dosync
      (println "sending message to act" role)
      (send actor act role)
      (if shouldFail (throw (new RuntimeException "Failing transaction"))))
    (catch Exception e (println "Failed transaction"))))

(def depp (agent ["Johnny Depp" ()]))

(try
  (sendMessageInTXN depp "Wonka" false)
  (sendMessageInTXN depp "Sparrow" true)  
  (await-for 1000 depp)
  (println "Roles played" (second @depp))
  (finally (shutdown-agents)))
