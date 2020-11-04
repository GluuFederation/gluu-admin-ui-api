#!/bin/sh

# This script creates and run the admin-api.

mvn clean package -DskipTests

# create run folder
rm -fr run
mkdir -p run/plugins

# copy artifacts to run folder
cp target/admin-api-*-SNAPSHOT.zip run/
cp plugins/test/target/test-plugin-*.zip run/plugins/
cp plugins/enabled.txt run/plugins/
cp plugins/disabled.txt run/plugins/

cd run

# unzip app
jar xf admin-api-*.zip
rm admin-api-*.zip

# start
mv admin-api-*-SNAPSHOT.jar admin-api.jar
java -jar admin-api.jar

cd -
