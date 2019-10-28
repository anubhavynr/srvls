#!/bin/bash
cd /home/ec2-user

AWS_REGION=$(curl -s http://169.254.169.254/latest/dynamic/instance-identity/document | jq -r '.region')
DB_HOST=$(aws ssm get-parameters --region $AWS_REGION --names DB_HOST | jq -r '.Parameters[0].Value')
DB_NAME=$(aws ssm get-parameters --region $AWS_REGION --names DB_NAME | jq -r '.Parameters[0].Value')
DB_USER=$(aws ssm get-parameters --region $AWS_REGION --names DB_USER | jq -r '.Parameters[0].Value')
DB_PASS=$(aws ssm get-parameters --region $AWS_REGION --names DB_PASS | jq -r '.Parameters[0].Value')

export AWS_REGION DB_HOST DB_NAME DB_USER DB_PASS

java -jar /home/ec2-user/monolith.jar > /dev/null 2> /dev/null < /dev/null &