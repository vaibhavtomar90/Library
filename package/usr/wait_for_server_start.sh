#!/bin/bash

function wait_for_server_start() {
    echo "Will now wait for server to start"
    wait_for_server_to_start_on 37300
    wait_for_server_to_start_on 37301
    echo "Server is up"
}

function wait_for_server_to_start_on() {
    port=$1
    local cmd="echo 2>/dev/null >/dev/tcp/localhost/${port}"
    local serverUp=0
    for i in `seq 1 1200` ; do
	sleep .1
        eval $cmd
	if [ $? -eq 0 ] ; then
		serverUp=1
		break
	fi
    done

    if [ $serverUp -eq 1 ] ; then
    	echo "Port ${port} is up"
    else
	echo "Port ${port} could not be upped"
	/etc/init.d/fk-pricing-kaizen stop
	/etc/init.d/fk-pricing-kaizen cleanup
	exit 1
    fi
}

wait_for_server_start
