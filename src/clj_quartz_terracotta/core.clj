(ns clj-quartz-terracotta.core
  (:import [org.quartz JobBuilder JobDataMap]))

(defn require-and-resolve [symbol-str]
  (let [[n h] (map symbol ((juxt namespace name) (symbol symbol-str)))]
    (require n)
    (ns-resolve (the-ns n) h)))

(defrecord RunAFunction []
  org.quartz.StatefulJob
  (execute [this ctx]
    (let [all (.getMergedJobDataMap ctx)
          f (require-and-resolve (get all "fn-to-call"))
          args (get all "args")]
      (apply f args))))

(defn ^JobBuilder fn-call [^JobBuilder jb ^clojure.lang.Symbol fn-to-call & args]
  (-> jb
    (.ofType RunAFunction)
    (.usingJobData (JobDataMap. {"fn-to-call" (str fn-to-call) "args" args}))))
