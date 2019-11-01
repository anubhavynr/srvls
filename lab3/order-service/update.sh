#!/bin/bash

S3_BUCKET=$1

FUNCTIONS=("saas-factory-srvls-wrkshp-orders-get-all-us-west-2" 
	"saas-factory-srvls-wrkshp-orders-get-by-id-us-west-2"
	"saas-factory-srvls-wrkshp-orders-insert-us-west-2"
	"saas-factory-srvls-wrkshp-orders-update-us-west-2"
	"saas-factory-srvls-wrkshp-orders-delete-us-west-2"
)

for FUNCTION in ${FUNCTIONS[@]}; do
	#echo $FUNCTION
	aws lambda --region us-west-2 update-function-code --function-name $FUNCTION --s3-bucket $S3_BUCKET --s3-key OrderService-lambda.zip
done
