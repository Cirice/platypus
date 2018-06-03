# platypus

URL shorner written in pure Clojure.

![Platypus](images/platypus.jpg)

## Building the project

You probabily need Leningen (v > 2.7) for building this project without docker.
To create an uberjar (a super jar with dependencies included) run the following in the project root path after installing Leiningen:

      $ lein uberjar    

## Usage with docker

Before running the program you need a ready-to-use Redis instance (preferably with persistent data storage).
For using this project just run the `docker-compose.yml` file.

      $ docker-compose up 

## Examples

See the [documentation](doc/intro.md) 
