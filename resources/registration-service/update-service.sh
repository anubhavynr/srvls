#!/bin/bash

REGION=$1
S3_BUCKET=$2
LAMBDA_CODE=RegistrationService-lambda.zip

FUNCTIONS=("saas-factory-srvls-wrkshp-reg-register-${REGION}")

for FUNCTION in ${FUNCTIONS[@]}; do
	#echo $FUNCTION
	aws lambda --region $REGION update-function-code --function-name $FUNCTION --s3-bucket $S3_BUCKET --s3-key $LAMBDA_CODE
done
