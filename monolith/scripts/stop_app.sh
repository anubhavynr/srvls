#!/bin/bash

kill (cat /home/ec2-user/monolith.pid)
rm /home/ec2-user/monolith.pid
