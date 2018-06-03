(ns platypus.utilities)

(require '[yaml.core :as yaml])
(require '[clojure.tools.logging :as log])

(defn nth' [xs n]
   "A simple function that extends 
the functionality of built-in function `nth`."
   (try
     (nth xs n)
     (catch Exception e ((constantly nil)))))

(defn read-conf!
  "An impure function for reading configurations as a yaml file.
returns an array map."
  [path]
  (log/info "Reading the configuration file: " path)
  (let [conf (yaml/from-file path)] 
       {:redis-ip (get-in conf ["redis-conf" "ip"]) 
        :redis-port (get-in conf ["redis-conf" "port"])
        :base-url (get-in conf ["base-url"])}))
