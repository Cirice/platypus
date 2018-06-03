(ns platypus.core
  (:gen-class))

(require '[org.httpkit.server :as http-kit]) 
(require '[taoensso.carmine :as redis]) 
(require '[cheshire.core :as json])
(require '[cheshire.parse :as parse])
(require '[platypus.utilities :as util])
(require '[clojure.tools.logging :as log])
(import  'clojure.lang.Murmur3) 
(import  'org.apache.commons.validator.routines.UrlValidator)


(def validator (UrlValidator. (into-array ["http" "https"])))
(def hash-url (comp (partial format "%x")
                    #(Murmur3/hashUnencodedChars %)))

(defn- json-request?
  "Checks if the request is of the type json."
  [request]
  (log/info "Checking if the request is of valid json type.")
  (if-let [type (get-in request [:headers "content-type"])]
    (not (empty? (re-find #"^application/(.+\+)?json" type)))))

(defn- read-json
  "Reads the body of a request of the type json into a valid Clojure map."
  [request & [{:keys [keywords? bigdecimals?]}]]
  (if (json-request? request)
    (if-let [body (:body request)]
      (let [body-string (slurp body)]
        (binding [parse/*use-bigdecimals?* bigdecimals?]
          (try
            [true (json/parse-string body-string keywords?)]
            (catch com.fasterxml.jackson.core.JsonParseException ex
[false nil])))))))

(defn create-short-url 
  "Creates a shortened URL given a valid one and a base URL."
  [base-url server-conn url]
  (log/info "Shortening URL: " url)  
  (let [rand-str (hash-url url)]
    (redis/wcar server-conn (redis/set (str base-url rand-str) url))
    (str base-url rand-str)))

(defn handle-create 
  "Checks if the request/URL given is valid  and if so shortens the URL."
  [base-url server-conn request]
  (let [jbody (read-json request)
        url (get-in (util/nth' jbody 1) ["url"])]
    (if-not (nil? url)
        (if (.isValid validator url) 
            {:status 200 :body (create-short-url base-url server-conn url)}
            {:status 401 :body "Invalid URL provided!"}) 
        {:status 400 :body "Invalid header or json body!"})))

(defn handle-redirect 
  "Given a request containing a valid
shortened URL it gives the original one."
  [base-url server-conn request]
  (let [jbody (read-json request)
        key (get-in (util/nth' jbody 1) ["url"])
        url (redis/wcar server-conn (redis/get key))]
    (log/info "Redirecting ...")
    (if-not (nil? key)   
      (if url
            {:status 301 :body url}
            {:status 404 :body "Unknown URL!"})
      {:status 400 :body "Invalid header or json body!"})))

(defn handler 
  [base-url server-conn {method :request-method :as req}]
  (case method
    :get ((partial handle-redirect base-url server-conn) req)
    :post ((partial handle-create base-url server-conn) req)))

(defn -main
  "The main function"
  [& args]
  (let [config (util/read-conf! "conf/default-conf.yml") 
        server-conn {:pool {} :spec {:host (:redis-ip config) :port (:redis-port config)}}
        base-url (:base-url config)
        handler' (partial handler base-url server-conn)
        port 6666]
    (log/info "Running the URL shortening server on port " port)   
    (http-kit/run-server handler' {:port port})))
