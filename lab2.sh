#!/bin/bash

# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy of this
# software and associated documentation files (the "Software"), to deal in the Software
# without restriction, including without limitation the rights to use, copy, modify,
# merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
# permit persons to whom the Software is furnished to do so.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
# INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
# PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
# HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
# OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
# SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

if ! [ -x "$(command -v jq)" ]; then
	echo "Installing jq"
    sudo yum install -y jq
fi

echo "Setting environment variables"
MY_AWS_REGION=$(aws configure list | grep region | awk '{print $2}')
echo "AWS Region = $MY_AWS_REGION"

WORKSHOP_STACK=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "SaaSFactoryServerlessSaaSWorkshopStack") | .OutputValue')
echo "Workshop stack = $WORKSHOP_STACK"

WORKSHOP_BUCKET=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "WorkshopBucket") | .OutputValue')
echo "Workshop bucket = $WORKSHOP_BUCKET"

LOAD_BALANCER_SG=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "LoadBalancerSecurityGroup") | .OutputValue')
echo "Application load balancer security group = $LOAD_BALANCER_SG"

PUBLIC_SUBNETS=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "PublicSubnets") | .OutputValue')
PUBLIC_SUBNETS=$(sed -e 's|,|\\,|' <<< $PUBLIC_SUBNETS)
echo "Public subnets = $PUBLIC_SUBNETS"

CODE_COMMIT_REPO=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "CodeCommitRepoName") | .OutputValue')
echo "CodeCommit repository name = $CODE_COMMIT_REPO"

CODE_COMMIT_CLONE_URL=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "CodeCommitCloneURL") | .OutputValue')
echo "CodeCommit clone URL = $CODE_COMMIT_CLONE_URL"

LAMBDA_ARN=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "AddDatabaseUserLambda") | .OutputValue')
echo "Add database user Lambda = $LAMBDA_ARN"

TEMPLATE_URL="https://${WORKSHOP_BUCKET}.s3.amazonaws.com/lab2.template"
echo "CloudFormation template URL = $TEMPLATE_URL"

echo
aws cloudformation create-stack --stack-name "${WORKSHOP_STACK}-lab2" --on-failure DO_NOTHING --capabilities CAPABILITY_NAMED_IAM --template-url "${TEMPLATE_URL}" --parameters \
ParameterKey=LoadBalancerSecurityGroup,ParameterValue="${LOAD_BALANCER_SG}" \
ParameterKey=LoadBalancerSubnets,ParameterValue="${PUBLIC_SUBNETS}" \
ParameterKey=CodeCommitRepoName,ParameterValue="${CODE_COMMIT_REPO}" \
ParameterKey=CodeCommitRepoURL,ParameterValue="${CODE_COMMIT_CLONE_URL}" \
ParameterKey=LambdaAddDatabaseUserArn,ParameterValue="${LAMBDA_ARN}" \
ParameterKey=WorkshopS3Bucket,ParameterValue="${WORKSHOP_BUCKET}"