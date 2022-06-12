#!/bin/bash

# 进入目录
cd ${project.basedir}

# 更新代码
if [ -z $gitpull ]; then
  echo "更新代码：${project.artifactId}-${project.version}"
  git pull
fi

# 编译代码
if [ -z $mvnbuild ]; then
  echo "编译代码：${project.artifactId}-${project.version}"
  mvn clean package install -D skipTests -P ${profile}
  #mvn -q clean package install -D skipTests -P ${profile}
fi

# 删除文件
rm -rf ${system.maven.run.path}/${project.artifactId}
# 运行目录
echo "拷贝文件：${project.artifactId}-${project.version}"
if [ ! -d "${system.maven.run.path}/${project.artifactId}" ]; then
  mkdir -p ${system.maven.run.path}/${project.artifactId}
fi
# 拷贝文件
cp -rf ${project.basedir}/target/${project.artifactId}-${project.version}/* ${system.maven.run.path}/${project.artifactId}
# 如果使用SpringBoot-repackage打包覆盖文件
#cp -rf ${project.basedir}/target/${project.artifactId}-${project.version}.jar ${system.maven.run.path}/${project.artifactId}

# 启动服务
echo "启动应用：${project.artifactId}-${project.version}"
cd ${system.maven.run.path}/${project.artifactId}
sh bin/startup.sh
