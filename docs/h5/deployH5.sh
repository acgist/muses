#!/bin/bash

# 进入目录
base=$(readlink -f $(dirname $0))
cd "$base/../"

# 项目名称
project="admin"

#git pull
# 强制更新远程代码
git fetch --all
git reset --hard origin/master
git pull

# 如果提交package-lock.json不用安装
npm install --unsafe-perm --allow-root
npm run build

# 删除文件
rm -rf "../deploy/h5/$project/dist/*"

# 替换文件
if [ ! -d "../deploy/h5/$project/dist" ]; then
  mkdir -p "../deploy/h5/$project/dist"
fi
cp -rf ./dist/* "../deploy/h5/$project/dist/"
