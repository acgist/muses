#!/bin/bash

# 等待任务
startTime=$(date +%s)
processNumber=0
while [ $processNumber -lt 1 ]
do
  sleep 1
  processNumber=`ps -aux | grep "${project.artifactId}" | grep -v grep | awk '{print $2}' | wc -l`
  if [ $processNumber -lt 1 ]; then
    echo ''
    echo '启动失败：${project.artifactId}'
    exit 0
  fi
  echo -n '.'
  processNumber=`netstat -anop | grep $(ps -aux | grep "${project.artifactId}" | grep -v grep | awk '{print $2}') | grep LISTEN | wc -l`
done
finishTime=$(date +%s)
processTime=$((startTime - finishTime))
echo ''
echo '启动耗时：$processTime S'
