#!/bin/bash
if ! [ -x "$(command -v jq)" ]; then
	echo "Installing jq"
    sudo yum install -y jq
fi

echo "Setting environment variables"
MY_AWS_REGION=$(aws configure list | grep region | awk '{print $2}')
echo "AWS Region = $MY_AWS_REGION"
API_GATEWAY_URL=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "ApiGatewayEndpoint") | .OutputValue')
echo "API Gateway Invoke URL = $API_GATEWAY_URL"
S3_WEBSITE_URL=$(aws cloudformation describe-stacks | jq -r '.Stacks[] | select(.Outputs != null) | .Outputs[] | select(.OutputKey == "WebsiteURL") | .OutputValue')
echo "S3 website URL = $S3_WEBSITE_URL"
S3_WEBSITE_BUCKET=$(echo $S3_WEBSITE_URL | sed -e 's@^.*://@@' -e 's/[\.].*$//')
echo "S3 website bucket = $S3_WEBSITE_BUCKET"
echo

# Edit src/shared/config.js in the ReactJS codebase
# set base_url to the Serverless SaaS REST API stage v1 invoke URL
echo "Configuring React to talk to API Gateway"
cd /home/ec2-user/environment/saas-factory-serverless-workshop/Lab\ 2
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
echo $S3_WEBSITE_URL

#aws s3api create-bucket --region $MY_AWS_REGION --bucket $WEBSITE_BUCKET --create-bucket-configuration LocationConstraint=$MY_AWS_REGION
#aws s3api put-bucket-website --bucket $WEBSITE_BUCKET --website-configuration '{"IndexDocument":{"Suffix":"index.html"}}'
