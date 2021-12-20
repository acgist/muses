## 命令

#### 编译

mvn clean package -D skipTests -P dev|test|release

#### 版本

mvn versions:set -D "newVersion=1.0.0"
mvn versions:update-child-modules
mvn versions:commit

> 执行`commit`将会删除备份文件