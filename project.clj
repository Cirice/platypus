(defproject platypus "0.1.0-alpha"
  :description "A simple URL shortner."
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.1.16"]
                 [com.taoensso/carmine "2.6.2"]
                 [commons-validator/commons-validator "1.4.0"]
                 [cheshire "5.7.1"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.slf4j/slf4j-log4j12 "1.7.1"]
                 [io.forward/yaml "1.0.6"]]
  :plugins [[lein-codox "0.10.3"]]
  :main ^:skip-aot platypus.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
