set JAVA_OPTS_MEM=-Xms${system.maven.xms} -Xmx${system.maven.xmx}
set JAVA_OPTS_EXT=-Dfile.encoding=${system.maven.encoding}
set JAVA_OPTS_APP=-Dspring.profiles.active=${profile}
set JAVA_OPTS=%JAVA_OPTS_MEM% %JAVA_OPTS_EXT% %JAVA_OPTS_APP% ${system.maven.jvm}
java %JAVA_OPTS% -jar .\lib\${project.artifactId}-${project.version}.jar