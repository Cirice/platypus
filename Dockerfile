FROM clojure

LABEL maintainer="abedi.hossein@protonmail.ch"


############################################################
# Setup user
############################################################

USER root

############################################################
# Setup environment variables
############################################################

ENV HOME_DIR /home

############################################################
# Creating directories/folders
############################################################

RUN mkdir -p  $HOME_DIR/platypus
ADD . $HOME_DIR/platypus

############################################################
# Running the uberjar
############################################################

WORKDIR $HOME_DIR/platypus

# Resolving dependencies
RUN lein deps
CMD ["lein", "run"]

