#!/bin/bash

cd ${project.basedir}

#git pull
# 强制更新远程代码
git fetch --all
git reset --hard origin/master
git pull

# 如果提交package-lock.json不用安装
#npm install
npm run build

# 删除文件
rm -rf ${system.maven.run.path}/h5

# 替换文件
cp -rf ${project.basedir} ${system.maven.run.path}/h5
