#!/bin/bash

# 启动目录
bin_abs_path=$(readlink -f $(dirname $0))
base=${bin_abs_path%/*}
cd $base
echo "启动目录：$base"

# 结束任务
if [ "${system.maven.run.type}" != "docker" ]; then
  sh bin/stop.sh
fi

# 启动应用
# -verbose:gc=-XX:+PrintGC
JAVA_OPTS_GC="-XX:+UseG1GC -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps"
JAVA_OPTS_MEM="-server -Xms${system.maven.xms} -Xmx${system.maven.xmx} -XX:SurvivorRatio=2"
JAVA_OPTS_EXT="-Dfile.encoding=${system.maven.encoding} -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true"
JAVA_OPTS_APP="-Dspring.profiles.active=${profile}"
JAVA_OPTS="$JAVA_OPTS_MEM $JAVA_OPTS_EXT $JAVA_OPTS_APP ${system.maven.jvm}"
echo "启动参数：$JAVA_OPTS"
if [ "${system.maven.run.type}" != "docker" ]; then
  # 其他启动
  nohup java $JAVA_OPTS -jar $base/lib/${project.artifactId}-${project.version}.jar > /dev/null 2>&1 &
else
  # 使用docker启动
  java $JAVA_OPTS -jar $base/lib/${project.artifactId}-${project.version}.jar
fi

# 等待任务
if [ "${system.maven.run.type}" != "docker" ]; then
  sh bin/wait.sh
fi

echo "启动成功：${project.artifactId}-${project.version}"
