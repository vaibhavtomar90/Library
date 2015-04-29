#!/bin/bash

if [ -f $PID_FILE ]; then
	echo  "$PACKAGE is running. pid `cat $PID_FILE`" 
else
	echo "$PACKAGE is not running..."
fi
