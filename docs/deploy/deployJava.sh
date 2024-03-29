#!/bin/bash

# 环境信息
env=$1
if [ -z $env ]; then
  echo "缺少环境信息：deployJava.sh dev|sit|uat|prd"
  exit 0
fi

# 参数信息
for arg in $*;
do
  case $arg in
    -cd|--copy-deploy)
      echo "拷贝部署命令"
      copyDeploy="true"
    ;;
    -sg|--skip-git)
      echo "跳过更新代码"
      skipGit="true"
    ;;
    -sm|--skip-mvn)
      echo "跳过编译代码"
      skipMvn="true"
    ;;
    -ss|--skip-startup)
      echo "跳过项目启动"
      skipStartup="true"
    ;;
    *)
      echo "未知参数：$arg"
    ;;
  esac
done

# 进入目录
base=$(readlink -f $(dirname $0))
cd "$base/../../"
echo "环境目录：$base"
echo "当前目录：$(pwd)"

# 更新代码
if [ -z $skipGit ]; then
  echo "更新代码"
  git pull
fi
declare -x gited=true

# 编译代码
if [ -z $skipMvn ]; then
  echo "编译代码"
  mvn clean package install -D skipTests -P $env -T 4
fi
declare -x mvned=true

# 部署应用
echo "部署项目"
tag=$(git tag | tail -1)
find ./ -type d -name "*-*-$tag" | while read path;
do
  echo "部署项目：${path##*/}"
  name=${path##*/}
  name=${name//-/}
  name=${name//$tag/}
  if [ "$copyDeploy" = "true" ]; then
    cp -rf "$path/bin/deploy.sh" "./deploy-${name}.sh"
  fi
  if [ -z $skipStartup ]; then
    sh "./deploy-${name}.sh"
  fi
done
