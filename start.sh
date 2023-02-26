#!/bin/sh
echo =================================
zsh: Automatic deployment script starts 
echo =================================


echo Stopping the previously running project
APP_NAME=reggie-web-manage

tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Stop Process...'
    kill -15 $tpid
fi
sleep 2
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Kill Process!'
    kill -9 $tpid
else
    echo 'Stop Success!'
fi



echo Stopping the previously running project
APP_NAME=reggie-web-app

tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Stop Process...'
    kill -15 $tpid
fi
sleep 2
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Kill Process!'
    kill -9 $tpid
else
    echo 'Stop Success!'
fi


echo Pull the latest code from the remote repository
cd /data/girui-takeout-Refactoring

echo Start pulling code
git pull origin master
echo Code pull complete

echo Start packing
output=`mvn clean package -Dmaven.test.skip=true`

cd /data/girui-takeout-Refactoring/reggie-web-manage/target

echo Start the web-manager
nohup java -jar reggie-web-manage.jar &> reggie-web-manage.log &
echo web-manager start success!

cd /data/girui-takeout-Refactoring/reggie-web-app/target

echo Start the web-app
nohup java -jar reggie-web-app-1.0-SNAPSHOT.jar &> reggie-web-app-1.0-SNAPSHOT.log &
echo web-app start success!


