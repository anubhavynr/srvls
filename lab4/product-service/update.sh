#!/bin/bash

S3_BUCKET=$1

FUNCTIONS=("saas-factory-srvls-wrkshp-products-get-all-us-west-2" 
	"saas-factory-srvls-wrkshp-products-get-by-id-us-west-2"
	"saas-factory-srvls-wrkshp-products-insert-us-west-2"
	"saas-factory-srvls-wrkshp-products-update-us-west-2"
	"saas-factory-srvls-wrkshp-products-delete-us-west-2"
)

for FUNCTION in ${FUNCTIONS[@]}; do
	#echo $FUNCTION
	aws lambda --region us-west-2 update-function-code --function-name $FUNCTION --s3-bucket $S3_BUCKET --s3-key ProductService-lambda.zip
done
