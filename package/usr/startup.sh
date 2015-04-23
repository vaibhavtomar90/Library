#!/bin/bash

[ -s $PID_FILE ] && echo "cant start again process is running [`cat $PID_FILE` ]" && exit 2

. /usr/share/$PACKAGE/prepare_runtime.sh
. /usr/share/$PACKAGE/prepare_options.sh


log_daemon_msg "Starting $DESC" "$NAME"

cd $PACKAGE_BASE
sudo -u $USER  $JAVA_HOME/jre/bin/java $JAVA_OPTS  -cp $CLASSPATH flipkart.pricing.apps.kaizen.boot.KaizenBooter server ${PRICING_HOME}/conf/pricing_kaizen.yaml 1>$OUT_FILE 2>$ERROR_FILE &
#storing pid into pit file.
echo $!>$PID_FILE

#changing file permission
chmod a+r $ERROR_FILE
chmod a+r $OUT_FILE
cd -
