# Introduction to platypus

A very simple URL shortner in Clojure.
For the moment it is possible for a user to post/get one URL per request.

###  Shortening a URL:
Shortening a URL is done by sending a Post request to Platypus.
Posting is pretty straightforward, every request should a header of type application/json and a valid body in the form of {"url": "url-to-be-shortened"} as shown below:

      $ curl -H "Content-Type: application/json" -X POST -d '{"url": "url-to-be-shortened"}' http://host-ip:host-port

For example:
 
      $ curl -H "Content-Type: application/json" -X POST -d '{"url":    "https://stackoverflow.com/questions/7172784/how-to-post-json-data-with-curl-from-terminal-commandline-to-test-spring-rest"}' http://localhost:6666
http://miras-tech.com/344d3f78%    

After posting with correct body format and header type you'll get a shortened URL in the form of 
`base-url+hash`, e.g. "http://miras-tech.com/a2743112%" where "http://miras-tech.com/" is the base URL and 
"a2743112%" is follwing hash part of the result. Along with the result a status code is returned where represent code for success and error (codes representing failure). For a description of the failure codes see the corresponding table of status codes.

###  Getting the orginal URL:

To get the orginal url you must issue a request in the following format:
 
      $  curl -H "Content-Type: application/json" -X GET -d '{"url": "shortened-url"}' http://host-ip:host-port

An example:
  
      $ curl -H "Content-Type: application/json" -X GET -d '{"url": "http://miras-tech.com/a2743112"}' http://localhost:6666 
https://www.google.com/search?q=dependencies&ie=utf-8&oe=utf-8&client=firefox-b-ab%            



### Status codes

Any status code other than 200 and 301 depicts an error.
The following table delivers a description for each status code.

| Status  Code   | Type          | Description                    |
| :------------: |:-------------:| :----------------------------: |
| 200            | Success       | URL shortened successfully     |
| 301            | Success       | Permanent URL redirection      |
| 400            | Error         | Invalid header type or body    |        
| 401            | Error         | Invalid URL                    |
| 404            | Error         | URL not found                  |
 
