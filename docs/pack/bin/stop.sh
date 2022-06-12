#!/bin/bash

# 结束任务
killIndex=0
processId=$(ps -aux | grep "${project.artifactId}" | grep -v grep | awk '{print $2}')
while [ ! -z "$processId" ]
do
  if [ $killIndex -le 10 ]; then
    # 优雅关机
    kill -15 $processId
  else
    # 强制关机
    kill -9 $processId
  fi
  sleep 1
  killIndex=$((killIndex+1))
  processId=$(ps -aux | grep "${project.artifactId}" | grep -v grep | awk '{print $2}')
done
