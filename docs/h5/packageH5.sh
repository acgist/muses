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

# 版本信息
tag=$(git tag | tail -1)

# 删除文件
rm -rf "../package/$project-$tag.tar.gz"

# 替换文件
if [ ! -d "../package" ]; then
  mkdir -p ../package
fi
tar -czvf "../package/$project-$tag.tar.gz" -C ./ "dist"
echo "打包完成："
ls -lh "../package/$project-$tag.tar.gz"
