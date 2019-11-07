#!/bin/bash
if ! [ -x "$(command -v jq)" ]; then
	echo "Installing jq"
    sudo yum install -y jq
fi

echo "Setting environment variables"
MY_AWS_REGION=$(aws configure list | grep region | awk '{print $2}')
echo "AWS Region = $MY_AWS_REGION"

WORKSHOP_STACK=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "SaaSFactoryServerlessSaaSWorkshopStack") | .OutputValue')
echo "Workshop stack = $WORKSHOP_STACK"

#WORKSHOP_BUCKET=$(aws cloudformation describe-stacks | jq -r '[.Stacks[] | select(.Parameters != null) | .Parameters[] | select(.ParameterKey == "WorkshopS3Bucket")][0] | .ParameterValue')
WORKSHOP_BUCKET=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "WorkshopBucket") | .OutputValue')
echo "Workshop bucket = $WORKSHOP_BUCKET"

LOAD_BALANCER_SG=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "LoadBalancerSecurityGroup") | .OutputValue')
echo "Application load balancer DNS = $LOAD_BALANCER"

PUBLIC_SUBNETS=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "PublicSubnets") | .OutputValue')
echo "Public subnets = $PUBLIC_SUBNETS"

CODE_COMMIT_REPO=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "CodeCommitRepoName") | .OutputValue')
echo "CodeCommit repository name = $CODE_COMMIT_REPO"

CODE_COMMIT_CLONE_URL=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "CodeCommitCloneURL") | .OutputValue')
echo "CodeCommit clone URL = $CODE_COMMIT_CLONE_URL"

TEMPLATE_URL="https://${WORKSHOP_BUCKET}.s3-${MY_AWS_REGION}.amazonaws.com/lab2.template"
echo "CloudFormation template URL = $TEMPLATE_URL"

echo
echo aws cloudformation create-stack --stack-name "${WORKSHOP_STACK}-lab2" --on-failure DO_NOTHING --capabilities CAPABILITY_NAMED_IAM --template-url "${TEMPLATE_URL}" --parameters \
ParameterKey=LoadBalancerSecurityGroup,ParameterValue=\""${LOAD_BALANCER_SG}"\" \
ParameterKey=LoadBalancerSubnets,ParameterValue="${PUBLIC_SUBNETS}" \
ParameterKey=CodeCommitRepoName,ParameterValue="${CODE_COMMIT_REPO}" \
ParameterKey=CodeCommitRepoURL,ParameterValue="${CODE_COMMIT_CLONE_URL}" \
ParameterKey=WorkshopS3Bucket,ParameterValue="${WORKSHOP_BUCKET}"