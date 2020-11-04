#!/bin/sh

# This script creates and run the jans-admin-ui-api.

mvn clean package -DskipTests

# create run folder
rm -fr run
mkdir -p run/plugins

# copy artifacts to run folder
cp target/jans-admin-ui-api-*-SNAPSHOT.zip run/
cp plugins/test/target/test-plugin-*.zip run/plugins/
cp plugins/enabled.txt run/plugins/
cp plugins/disabled.txt run/plugins/

cd run

# unzip app
jar xf jans-admin-ui-api-*.zip
rm jans-admin-ui-api-*.zip

# start
mv jans-admin-ui-api-*-SNAPSHOT.jar jans-admin-ui-api.jar
java -jar jans-admin-ui-api.jar

cd -
