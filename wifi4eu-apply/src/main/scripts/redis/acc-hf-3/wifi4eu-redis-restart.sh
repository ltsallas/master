#!/bin/bash

TARGET="redis-server";
PIDGO=`pidof $TARGET`;
echo $PIDGO

if [ ! x$PIDGO = "x" ] ; then
        echo "Restarting $TARGET";
        kill $PIDGO;
else
	echo "Starting $TARGET";
fi

nohup redis-server /home/client-acc-dev/redis/server.conf &> logging/redis-server.out &

TARGET="redis-sentinel";
PIDGO=`pidof $TARGET`;
echo $PIDGO

if [ ! x$PIDGO = "x" ] ; then
        echo "Restarting $TARGET";
        kill $PIDGO;
else
        echo "Starting $TARGET";
fi

nohup redis-server /home/client-acc-dev/redis/sentinel.conf --sentinel &> logging/redis-sentinel.out&

