JAVA_OPTS_MEM="-Xms${system.maven.xms} -Xmx${system.maven.xmx}"
JAVA_OPTS_EXT="-D file.encoding=${system.maven.encoding}"
JAVA_OPTS_APP="-D spring.profiles.active=${profile}"
JAVA_OPTS="$JAVA_OPTS_MEM $JAVA_OPTS_EXT $JAVA_OPTS_APP ${system.maven.jvm}"
java $JAVA_OPTS -jar ./lib/${project.artifactId}-${project.version}.jar
