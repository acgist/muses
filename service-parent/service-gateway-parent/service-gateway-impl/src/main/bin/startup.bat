set JAVA_OPTS_MEM=-Xms256m -Xmx512m
set JAVA_OPTS_EXT=-Dfile.encoding=UTF-8
set JAVA_OPTS_APP=-Dspring.profiles.active=${profile}

set JAVA_OPTS= %JAVA_OPTS_MEM% %JAVA_OPTS_EXT% %JAVA_OPTS_APP%

java %JAVA_OPTS% -jar .\lib\${project.artifactId}-${project.version}.jar
