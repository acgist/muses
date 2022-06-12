#!/bin/bash

# 进入目录
cd ${project.basedir}

# 更新代码
echo '更新代码'
git pull
declare -x gitpull=true

# 编译代码
echo '编译代码'
mvn clean packge install -D skipTests -P ${profile}
declare -x mvnbuild=true

# 启动应用
echo '启动应用'
#sh deployBoot.sh
#sh deployGateway.sh
#sh deployWeb.sh
#sh deployRest.sh
