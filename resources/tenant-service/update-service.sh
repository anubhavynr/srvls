#!/bin/bash

REGION=$1
S3_BUCKET=$2
LAMBDA_CODE=TenantService-lambda.zip

FUNCTIONS=("saas-factory-srvls-wrkshp-tenants-get-all-${REGION}" 
	"saas-factory-srvls-wrkshp-tenants-get-by-id-${REGION}"
	"saas-factory-srvls-wrkshp-tenants-insert-${REGION}"
	"saas-factory-srvls-wrkshp-tenants-update-${REGION}"
	"saas-factory-srvls-wrkshp-tenants-delete-${REGION}"
	"saas-factory-srvls-wrkshp-tenants-next-db-${REGION}"
	"saas-factory-srvls-wrkshp-tenants-update-userpool-${REGION}"
)

for FUNCTION in ${FUNCTIONS[@]}; do
	#echo $FUNCTION
	aws lambda --region $REGION update-function-code --function-name $FUNCTION --s3-bucket $S3_BUCKET --s3-key $LAMBDA_CODE
done
