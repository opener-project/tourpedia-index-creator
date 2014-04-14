# This script install the Tourpedia Index tool that uses the Apache Lucene library:
#   http://lucene.apache.org/
# Author: Andoni Azpeitia (aazpeitia@vicomtech.org) - Vicomtech-IK4 (http://www.vicomtech.es/)

mvn package
EXEC=$?
if [ $EXEC -ne 0 ]
then
    echo "Error installing Tourpedia Index Tool"
    exit
fi
