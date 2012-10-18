(ns clj-quartz-terracotta.core
  (:import [org.quartz JobBuilder JobDataMap JobExecutionContext]))

(defn require-and-resolve [symbol-str]
  (let [[n sym] (map symbol ((juxt namespace name) (symbol symbol-str)))]
    (require n)
    (ns-resolve (the-ns n) sym)))

(defrecord RunAFunction []
  org.quartz.StatefulJob
  (execute [this ctx]
    (let [all (.getMergedJobDataMap ctx)
          f (require-and-resolve (get all "fn-to-call"))
          args (get all "args")]
      (apply f args))))

(defn ^JobBuilder fn-call
  "Will configure a Quartz job to run a function. Note that the fn-to-call must be a fully qualified
  symbol so it can be resolved if the job is run in a different JVM."
  [^JobBuilder jb ^clojure.lang.Symbol fn-to-call & args]
  (-> jb
    (.ofType RunAFunction)
    (.usingJobData (JobDataMap. {"fn-to-call" (str fn-to-call) "args" args}))))
