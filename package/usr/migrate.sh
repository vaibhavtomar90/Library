#!/bin/bash

. /usr/share/$PACKAGE/prepare_runtime.sh
. /usr/share/$PACKAGE/prepare_options.sh


echo "Running sql migrations"
sudo -u $USER $JAVA_HOME/jre/bin/java $JAVA_OPTS -cp $CLASSPATH flipkart.pricing.apps.kaizen.boot.KaizenBooter db migrate /usr/share/$PACKAGE/conf/*.yaml 1>${OUT_FILE}.migrate 2>${ERROR_FILE}.migrate
echo "Running migration tagging"
sudo -u $USER $JAVA_HOME/jre/bin/java $JAVA_OPTS -cp $CLASSPATH flipkart.pricing.apps.kaizen.boot.KaizenBooter db tag /usr/share/$PACKAGE/conf/*.yaml `date +"DATE_%d_%m_%Y_TIME_%H_%M_%S"` 1>>${OUT_FILE}.txt 2>>${ERROR_FILE}.txt
echo "Successfully done migrations"
