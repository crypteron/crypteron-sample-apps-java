#!/bin/bash
projectName=${1:-cipherdb-agent-sample-hibernate5}
if mvn package --projects $projectName --also-make ; then
cd $projectName
  jarFile=$(ls target/$projectName*.jar | tail -1)
  java -jar $jarFile
fi