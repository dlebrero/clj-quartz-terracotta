# clj-quartz-terracotta

Simple Clojure library to avoid AOT compilation when using Quartz and Terracotta.

## Usage

Add clj-quartz-terracotta to your project.clj:

```clojure
[clj-quartz-terracotta "0.2.0"]
```

Plus your favourite version of [Quartzite](http://clojurequartz.info/):

```clojure
[clojurewerkz/quartzite "1.0.1"]
```

Then just use the `fn-call` function to create the job:

```clojure

    (ns some-namespace
      (:require [clojurewerkz.quartzite.jobs :as j]
                [clj-quartz-terracotta.core :as tc]))

     (def job (j/build
                (tc/fn-call `println "hi there")
                (j/with-identity (j/key "some.job.identity")))
```

Note that the first parameter must be a full qualified function, as it will need to be resolved in any JVM in the Terracotta cluster. The created job will be a [StatefulJob](http://quartz-scheduler.org/api/2.0.0/org/quartz/StatefulJob.html)


## License

Copyright © 2012 Daniel Lebrero

Distributed under the Eclipse Public License, the same as Clojure.
