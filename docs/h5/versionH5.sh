#!/bin/bash

# 版本信息
version=$1
if [ -z $version ]; then
  echo "缺少版本信息：versionH5.sh 1.0.0"
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
cd "$base/../"
echo "环境目录：$base"
echo "当前目录：$(pwd)"

# 设置标签
echo "设置Git标签（$version）"
git tag $version

# 提交版本
echo "提交版本信息（$version）"
git add -u
git commit -m "[!] 发布版本（$version）"
git push --tags
