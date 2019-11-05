#!/bin/bash
cd /home/ec2-user

# Get the tenant who owns this environment
PREFIX=''
if [ -f /etc/profile.d/saas.sh ]; then
  . /etc/profile.d/saas.sh
  PREFIX="${TENANT_ID}_"
fi

AWS_REGION=$(curl -s http://169.254.169.254/latest/dynamic/instance-identity/document | jq -r '.region')
DB_HOST=$(aws ssm get-parameters --region $AWS_REGION --names "${PREFIX}DB_HOST" | jq -r '.Parameters[0].Value')
DB_NAME=$(aws ssm get-parameters --region $AWS_REGION --names "${PREFIX}DB_NAME" | jq -r '.Parameters[0].Value')
DB_USER=$(aws ssm get-parameters --region $AWS_REGION --names "${PREFIX}DB_USER" | jq -r '.Parameters[0].Value')
DB_PASS="${PREFIX}DB_PASS"

export AWS_REGION DB_HOST DB_NAME DB_USER DB_PASS

java -jar /home/ec2-user/application.jar > /dev/null 2> /dev/null < /dev/null &