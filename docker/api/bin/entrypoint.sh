#!/bin/bash

echo "Bootstrapping container.";
echo "Environment has been configured. Running the application by:"
echo "java $JAVA_OPTS -jar cooking-instruction-0.0.1-SNAPSHOT.jar $@"
java "$JAVA_OPTS" -jar cooking-instruction-0.0.1-SNAPSHOT.jar $@