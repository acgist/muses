#!/bin/bash

# 启动目录
bin_abs_path=$(readlink -f $(dirname $0))
base=${bin_abs_path%/*}
cd $base
echo "启动目录：$base"

# 运行环境
runType="${system.maven.run.type}"

# 结束任务
if [ $runType != "docker" ]; then
  sh bin/stop.sh
fi

# 启动参数
JAVA_OPTS_GC="-XX:+UseG1GC -Xlog:gc:./logs/gc.log:time,level"
JAVA_OPTS_MEM="-server ${system.maven.jvm.mem}"
JAVA_OPTS_EXT="-Dfile.encoding=${system.maven.encoding} -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true"
JAVA_OPTS_APP="-Dspring.profiles.active=${profile}"
JAVA_OPTS="$JAVA_OPTS_MEM $JAVA_OPTS_EXT $JAVA_OPTS_APP ${system.maven.jvm.arg}"
echo "启动参数：$JAVA_OPTS"

# 启动应用
echo "启动应用：${project.artifactId}-${project.version}"
if [ $runType != "docker" ]; then
  # 其他启动
  nohup java $JAVA_OPTS -jar $base/lib/${project.artifactId}-${project.version}.jar > /dev/null 2>&1 &
else
  # 使用docker启动：后台启动不能查看控制台的信息
  java $JAVA_OPTS -jar $base/lib/${project.artifactId}-${project.version}.jar
fi

# 等待任务
if [ $runType != "docker" ]; then
  sh bin/wait.sh
else
  echo "启动成功：${project.artifactId}-${project.version}"
fi

echo "--------------------------------"
