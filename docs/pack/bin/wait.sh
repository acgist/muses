#!/bin/bash

# 等待任务
startTime=$(date +%s)
processNumber=`ps -aux | grep "${project.artifactId}" | grep -v grep | awk "{print $2}" | wc -l`
processPortNumber=`netstat -anop | grep $(ps -aux | grep "${project.artifactId}" | grep -v grep | awk "{print $2}") | grep LISTEN | wc -l`
while [ $processNumber -ge 1 && $processPortNumber -lt 1 ]
do
  sleep 1
  processNumber=`ps -aux | grep "${project.artifactId}" | grep -v grep | awk "{print $2}" | wc -l`
  processPortNumber=`netstat -anop | grep $(ps -aux | grep "${project.artifactId}" | grep -v grep | awk "{print $2}") | grep LISTEN | wc -l`
  echo -n "."
done
  echo ""
if [ $processNumber -lt 1 ]; then
  echo "启动失败：${project.artifactId}-${project.version}"
  exit 0
else
  finishTime=$(date +%s)
  processTime=$((startTime - finishTime))
  echo "启动耗时：$processTime S"
fi
