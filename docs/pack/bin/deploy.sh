echo '正在启动rest'
num=`ps aux | grep 'rest' | grep java | tr -s ' ' | cut -d ' ' -f 2 | wc -l`
if [ ${num} -ge 1 ]; then
  ps aux | grep 'rest' | grep java | tr -s ' ' | cut -d ' ' -f 2 | xargs kill -9
fi
cp -rf /data/project/code/muses/rest/target/rest-1.0.0.jar /data/project/rest/
cp -rf /data/project/code/muses/rest/target/rest-1.0.0/* /data/project/rest/
cd /data/project/rest/
nohup sh bin/startup.sh > /dev/null 2>&1 &
num=0
while [ ${num} -lt 1 ]
do
  sleep 1
  num=`ps aux | grep 'rest' | grep java | tr -s ' ' | cut -d ' ' -f 2 | wc -l`
  if [ ${num} -lt 1 ]; then
    echo ''
    echo '启动失败rest'
    exit 0
  fi
  echo -n '.'
  num=`netstat -anop | grep $(ps aux | grep 'rest' | grep java | tr -s ' ' | cut -d ' ' -f 2) | grep LISTEN | wc -l`
done
echo ''
echo '启动完成rest'
