#!/bin/bash

# 环境信息
env=$1
if [ -z $env ]; then
  echo "缺少环境信息：packageJava.sh dev|sit|uat|prd"
  exit 0
fi

# 进入目录
base=$(readlink -f $(dirname $0))
cd "$base/../../"
echo "环境目录：$base"
echo "当前目录：$(pwd)"

# 更新代码
echo "更新代码"
git pull

# 编译代码
echo "编译代码"
mvn clean package install -D skipTests -P $env -T 4

# 打包代码
echo "开始打包：$env"
if [ ! -d "../package" ]; then
  mkdir -p "../package"
fi
tag=$(git tag | tail -1)
find ./ -type d -name "*-*-$tag" | while read path;
do
  echo "打包项目：${path##*/}"
  cp -rf "$path.jar" "$path"
  tar -czvf "../package/${path##*/}.tar.gz" -C "${path%/*}" "${path##*/}"
  echo "打包完成："
  ls -lh "../package/${path##*/}.tar.gz"
done
