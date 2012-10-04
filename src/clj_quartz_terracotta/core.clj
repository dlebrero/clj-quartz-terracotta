(ns clj-quartz-terracotta.core
  (:require [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.conversion :as conv])
  (:import org.quartz.JobBuilder)
  (:use [clojurewerkz.quartzite.stateful]))

(defn require-and-resolve [symbol-str]
  (let [[n h] (map symbol ((juxt namespace name) (symbol symbol-str)))]
    (require n)
    (ns-resolve (the-ns n) h)))

(def-stateful-job RunAFunction
  [ctx]
  (println (conv/from-job-data ctx))
  (let [all (conv/from-job-data ctx)
        f (require-and-resolve (get all "fn-to-call"))
        args (get all "args")]
    (apply f args)))

(defn ^JobBuilder fn-call [^JobBuilder jb ^clojure.lang.Symbol fn-to-call & args]
  (-> jb
    (j/of-type RunAFunction)
    (j/using-job-data {"fn-to-call" (str fn-to-call) "args" args})))