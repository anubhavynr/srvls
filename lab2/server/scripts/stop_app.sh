#!/bin/bash

PID_FILE=/home/ec2-user/application.pid
if [ -f "$PID_FILE" ]; then
    pkill -F $PID_FILE
    rm -f $PID_FILE
fi
