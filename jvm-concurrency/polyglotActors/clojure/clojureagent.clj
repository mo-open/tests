(comment START:actmethod)
(defn act [actor role]
  (let [[name roles] actor]
    (println name "playing role of" role)
    (. Thread sleep 2000)
    [name (conj roles role)]))
(comment END:actmethod)

(comment START:agents)
(try
  (def depp (agent ["Johnny Depp" ()]))
  (def hanks (agent ["Tom Hanks" ()]))
  (comment END:agents)

  (comment START:send)
  (send depp act "Wonka")
  (send depp act "Sparrow")
  (send hanks act "Lovell")
  (send hanks act "Gump")
  (comment END:send)

  (comment START:await)
  (println "Let's wait for them to play")
  (await-for 5000 depp hanks)
  (comment END:await)

  (comment START:print)
  (println "Let's see the net effect")
  (println (first @depp) "played" (second @depp))
  (println (first @hanks) "played" (second @hanks))
  (comment END:print)

  (comment START:shutdown)
  (finally (shutdown-agents)))
(comment END:shutdown)
