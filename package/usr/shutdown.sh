#!/bin/bash
COUNT=0
echo -ne "Stopping "
while [ $COUNT -lt 20 ]; do
 echo -ne " . "
 APP_PID=`pgrep -u $PACKAGE` || true
 [ -z $APP_PID ] && break
 kill $APP_PID
 sleep 1s
 COUNT=$(( $COUNT + 1 ))
done

[ $COUNT -eq 20 ] && echo "Force killing" && kill -9 $APP_PID
[ -f $PID_FILE ] && rm -f $PID_FILE && echo "\nDone"
