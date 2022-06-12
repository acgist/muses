#!/bin/bash

# 等待任务
startTime=$(date +%s)
processId=`ps -aux | grep "${project.artifactId}" | grep -v grep | awk "{print $2}"`
if [ ! -z "$processId" ]; then
  processPortNumber=`netstat -anop | grep $processId | grep LISTEN | wc -l`
  while [ ! -z "$processId" ] && [ $processPortNumber -lt 1 ]
  do
    sleep 1
    processId=`ps -aux | grep "${project.artifactId}" | grep -v grep | awk "{print $2}"`
    processPortNumber=`netstat -anop | grep $processId | grep LISTEN | wc -l`
    echo -n "."
  done
  echo ""
fi
if [ -z "$processId" ]; then
  echo "启动失败：${project.artifactId}-${project.version}"
  exit 0
else
  finishTime=$(date +%s)
  processTime=$((startTime - finishTime))
  echo "启动耗时：$processTime S"
fi
