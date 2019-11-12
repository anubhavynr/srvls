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
API_GATEWAY_URL=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "ApiGatewayEndpointLab2") | .OutputValue')
echo "API Gateway Invoke URL = $API_GATEWAY_URL"
S3_WEBSITE_BUCKET=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "WebsiteS3Bucket") | .OutputValue')
echo "S3 website bucket = $S3_WEBSITE_BUCKET"
CLOUDFRONT_DISTRIBUTION=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "CloudFrontDistributionDNS") | .OutputValue')
echo "CloudFront distribution URL = $CLOUDFRONT_DISTRIBUTION"
echo

# Edit src/shared/config.js in the ReactJS codebase
# set base_url to the Serverless SaaS REST API stage v1 invoke URL
echo "Configuring React to talk to API Gateway"
cd /home/ec2-user/environment/saas-factory-serverless-workshop/lab2/client
sed -i -r -e 's|(^\s+)(base_url: )(process.env.REACT_APP_BASE_URL,)|//\1\2\3\n\1\2"'"${API_GATEWAY_URL}"'"|g' src/shared/config.js

echo
echo "Installing NodeJS dependencies"
npm install

echo
echo "Building React app"
npm run build

echo
echo "Uploading React app to S3 website bucket"
cd build
aws s3 sync --delete --acl public-read . s3://$S3_WEBSITE_BUCKET

#echo "http://$WEBSITE_BUCKET-website-$MY_AWS_REGION.amazonaws.com"
echo
echo "Access your website at..."
echo $CLOUDFRONT_DISTRIBUTION
echo