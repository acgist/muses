#!/bin/bash

# 进入目录
cd ${project.basedir}

# 更新代码
if [ $gitpull -ne true ]; then
  echo '更新代码：${project.artifactId}-${project.version}'
  git pull
fi

# 编译代码
if [ $mvnbuild -ne true ]; then
  echo '编译代码：${project.artifactId}-${project.version}'
  mvn clean packge install -D skipTests -P ${profile}
fi
