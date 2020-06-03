#!/bin/bash
cd /opt/app
if [ -z "${java_runtime_arguments}" ]; then
  java  -Xms128m -Xmx768m -jar /opt/app/lib/magellan-nbi.jar
else
  java  $java_runtime_arguments -jar /opt/app/lib/magellan-nbi.jar
fi
