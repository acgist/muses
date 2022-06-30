#!/bin/bash

# 配置参数
for i in $*;
do
  case $i in
    -sg|--skip-git)
      echo "跳过更新代码"
      skipGit="true"
    ;;
    -sm|--skip-mvn)
      echo "跳过编译代码"
      skipMvn="true"
    ;;
    *)
      echo "未知参数：$i"
    ;;
  esac
done

# 进入目录
cd ${project.basedir}

# 更新代码
if [ -z $skipGit ]; then
  echo "更新代码"
  git pull
fi
declare -x gitpull=true

# 编译代码
if [ -z $skipMvn ]; then
  echo "编译代码"
  mvn clean package install -D skipTests -P ${profile} -T 4
  #mvn -q clean package install -D skipTests -P ${profile} -T 4
fi
declare -x mvnbuild=true

# 启动应用
cd ${project.basedir}
echo "启动应用"
#sh deployBoot.sh
#sh deployGateway.sh
#sh deployWeb.sh
#sh deployRest.sh
