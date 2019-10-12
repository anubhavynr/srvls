#!/bin/bash

# Supported regios (Cloud9 limitation)
# us-east-1      N Virginia 
# us-east-2      Ohio
# us-west-2      Oregon
# eu-west-1      Ireland
# eu-central-1   Frankfurt
# ap-southeast-1 Singapore
# ap-northeast-1 Tokyo

sudo yum install -y java-1.8.0-openjdk-devel
sudo update-alternatives --set java /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java
sudo update-alternatives --set javac /usr/lib/jvm/java-1.8.0-openjdk.x86_64/bin/javac

sudo curl -L http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -o /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven

MY_REGION=$(aws configure list | grep region | awk '{print $2}')
DIST_REPO=https://github.com/brtrvn/srvls.git

git config --global credential.helper '!aws codecommit credential-helper $@'
git config --global credential.UseHttpPath true

git clone --mirror $DIST_REPO github-dist
cd github-dist
git push https://git-codecommit.$MY_REGION.amazonaws.com/v1/repos/saas-factory-serverless-workshop --all
cd ..
rm -rf github-dist
git clone https://git-codecommit.$MY_REGION.amazonaws.com/v1/repos/saas-factory-serverless-workshop
cd saas-factory-serverless-workshop
