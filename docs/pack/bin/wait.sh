#!/bin/bash

# 等待任务
startTime=$(date +%s)
processId=$(ps -aux | grep "${project.artifactId}" | grep -v grep | awk '{print $2}')
if [ ! -z "$processId" ]; then
  waitIndex=0
  processPortNumber=$(netstat -anop | grep $processId | grep LISTEN | wc -l)
  while [ $waitIndex -le 120 ] && [ ! -z "$processId" ] && [ $processPortNumber -lt 1 ]
  do
    sleep 1
    waitIndex=$((waitIndex+1))
    processId=$(ps -aux | grep "${project.artifactId}" | grep -v grep | awk '{print $2}')
    processPortNumber=$(netstat -anop | grep $processId | grep LISTEN | wc -l)
    echo -n "."
  done
  echo ""
fi
if [ $processPortNumber -lt 1 ]; then
  echo "启动失败：${project.artifactId}-${project.version}"
  sh bin/stop.sh
  exit 0
else
  finishTime=$(date +%s)
  processTime=$((finishTime-startTime))
  echo "启动成功：${project.artifactId}-${project.version} - $processId"
  echo "启动端口：$(netstat -anop | grep $processId | grep LISTEN | awk '{print $4}')"
  echo "启动耗时：$processTime S"
fi
