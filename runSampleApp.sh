#!/bin/sh

projectName=${1:-cipherobject-sample}

## Minimal sanity check: Is appSecret changed from repo default?
propFile=$projectName/src/main/resources/crypteron.properties
if grep -q "appSecret=Replace" $propFile
then
  echo 
  echo "Please setup crypteron.appSecret setup in \""$propFile"\""
  echo "by running  setAppSecret.sh or followning the readme.md."
  echo
  echo "Aborting ! "
  echo
  exit 1
fi

## Kick off sample app
if mvn package --projects $projectName --also-make ; then
  cd $projectName
  jarFile=$(ls target/$projectName*.jar | tail -1)
  java -jar $jarFile
  cd -
fi