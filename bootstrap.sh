#N Virginia
#Ohio
#Oregon
#Ireland
#Frankfurt
#Singapore
#Tokyo

git config --global credential.helper '!aws codecommit credential-helper $@'
git config --global credential.UseHttpPath true

git clone --mirror https://github.com/brtrvn/srvls.git github-dist
cd github-dist
git push https://git-codecommit.us-west-2.amazonaws.com/v1/repos/saas-factory-serverless-workshop --all
cd ..
rm -rf github-dist
git clone https://git-codecommit.us-west-2.amazonaws.com/v1/repos/saas-factory-serverless-workshop
cd saas-factory-serverless-workshop

