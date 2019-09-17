#!/bin/bash
AWS_REGION=$(curl -s http://169.254.169.254/latest/dynamic/instance-identity/document | jq -r .region)
DB_HOST=$(aws ssm get-parameters --region $AWS_REGION --names DB_HOST --query Parameters[0].Value)
DB_NAME=$(aws ssm get-parameters --region $AWS_REGION --names DB_NAME --query Parameters[0].Value)
DB_USER=$(aws ssm get-parameters --region $AWS_REGION --names DB_USER --query Parameters[0].Value)
DB_PASS=$(aws ssm get-parameters --region $AWS_REGION --names DB_PASS --query Parameters[0].Value)
java -jar /home/ec2-user/monolith.jar > /dev/null 2> /dev/null < /dev/null &