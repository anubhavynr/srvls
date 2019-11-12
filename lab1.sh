#!/bin/bash

WORKSHOP_BUCKET=$1

if [ ! -d resources ]; then
	echo "Can't find resources directory"
	exit 1
fi

cd resources

for LAMBDA in $(ls -d */); do
	if [ $LAMBDA != "custom-resources/" ]; then
		cd $LAMBDA
		mvn
		cd ..
	fi
done

find . -type f -name '*-lambda.zip' -exec aws s3 cp {} s3://$WORKSHOP_BUCKET \;

cd custom-resources
for CFN_RES in $(ls -d */); do
	cd $CFN_RES
	mvn
	cd ..
done

find . -type f -name '*.jar' -a ! -name '*original*' -exec aws s3 cp {} s3://$WORKSHOP_BUCKET \;

cd ..
for CFN_TEMPLATE in $(ls *.template); do
	aws s3 cp $CFN_TEMPLATE s3://$WORKSHOP_BUCKET
done

read -p "Launch Workshop CloudFormation Template? (y/n)?" ANSWER
case ${ANSWER:0:1} in
	y|Y )
		echo Yes
		# Auto expand
		# Named IAM
	;;
	* )
		exit 0
	;;
esac

exit 0
