#!/bin/bash

bin_abs_path=$(readlink -f $(dirname $0))
base=${bin_abs_path%/*}
cd $base

JAVA_OPTS_MEM="-server -Xms${system.maven.xms} -Xmx${system.maven.xmx}"
JAVA_OPTS_EXT="-D file.encoding=${system.maven.encoding}"
JAVA_OPTS_APP="-D spring.profiles.active=${profile}"
JAVA_OPTS="$JAVA_OPTS_MEM $JAVA_OPTS_EXT $JAVA_OPTS_APP ${system.maven.jvm}"

java $JAVA_OPTS -jar $base/${project.artifactId}-${project.version}.jar
