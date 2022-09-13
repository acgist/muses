#!/bin/bash

# 版本信息
version=$1
if [ -z $version ]; then
  echo "缺少版本信息：versionJava.sh 1.0.0"
  exit 0
fi

# 提示确认
read -r -p "确认是否发布版本（$version）？[y/n]" confirm
case $confirm in
  [yY])
    echo "开始版本设置（$version）"
    ;;
  *)
  echo "退出版本设置（$version）"
    exit 0
    ;;
esac

# 进入目录
base=$(readlink -f $(dirname $0))
cd "$base/../../"

# 设置标签
echo "设置Git标签（$version）"
git tag $version

# 设置版本
echo "设置Maven版本（$version）"
mvn versions:set -DnewVersion=$version
mvn -N versions:update-child-modules
mvn versions:commit

# 提交版本
echo "提交版本信息（$version）"
git add -A
git commit -m "[!] 发布版本（$version）"
git push --tags
