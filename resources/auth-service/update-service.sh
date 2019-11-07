#!/bin/bash

REGION=$1
S3_BUCKET=$2
LAMBDA_CODE=AuthService-lambda.zip

FUNCTIONS=("saas-factory-srvls-wrkshp-auth-signin-${REGION}")

for FUNCTION in ${FUNCTIONS[@]}; do
	#echo $FUNCTION
	aws lambda --region $REGION update-function-code --function-name $FUNCTION --s3-bucket $S3_BUCKET --s3-key $LAMBDA_CODE
done
