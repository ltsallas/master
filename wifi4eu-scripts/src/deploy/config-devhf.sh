# Parameters
ENV=DEVHF
LOCAL_SOURCECODE=.
REMOTE_COPYFOLDER=/home/everis/deploy/auto
FO_WAR1_FOLDER=/wifi4eu-portal/wifi4eu-portal-web/target/*.war
FO_WAR2_FOLDER=/wifi4eu-public-portal/wifi4eu-public-portal-web/target/*.war
FO_TOMCAT_1_CRED=everis@202.1.1.7
FO_TOMCAT_LIST="$FO_TOMCAT_1_CRED"
FO_TOMCAT_PATH=/opt/tomcat-latest
FO_TOMCAT_SERVICE=tomcat8
BO_WAR1_FOLDER=/wifi4eu-dgconn/wifi4eu-dgconn-web/target/*.war
BO_TOMCAT_1_CRED=everis@202.1.1.7
BO_TOMCAT_PATH=/opt/tomcat-latest
BO_TOMCAT_SERVICE=tomcat8
VARNISH_1_CRED=everis@202.1.1.7
VARNISH_2_CRED=everis@202.1.1.7

PEM_FO_CERT_PATH=/usr/local/lib/security/jenkins-pems/azurevm.pem
PEM_BO_CERT_PATH=/usr/local/lib/security/jenkins-pems/azurevm.pem
PEM_VARNISH_CERT_PATH=/usr/local/lib/security/jenkins-pems/azurevm.pem