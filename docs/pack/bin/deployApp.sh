#!/bin/bash

# 进入目录
cd ${project.basedir}

# 更新代码
if [ -z $gitpull ]; then
  echo '更新代码：${project.artifactId}-${project.version}'
  git pull
fi

# 编译代码
if [ -z $mvnbuild ]; then
  echo '编译代码：${project.artifactId}-${project.version}'
  mvn clean package install -D skipTests -P ${profile}
  #mvn -q clean package install -D skipTests -P ${profile}
fi
