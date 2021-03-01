#!/bin/sh
# ‘/home/snatchub/Documents/Share/jar as service test/javaPlatform/bin/java’ -Xmx100M -jar os-service-1.0-SNAPSHOT.jar ‘amazing’ prod
SERVICE_NAME=linuxmonitor
PATH_TO_JAVA=”/home/iskander/IdeaProjects/LinuxMonitor/out/artifacts/LinuxMonitorUi_jar/LinuxMonitorUi.jar as service linuxmonitor/javaPlatform/bin/java”
JVM_PARAMS=-Xmx100M
PATH_TO_JAR=/home/iskander/IdeaProjects/LinuxMonitor/out/artifacts/LinuxMonitorUi_jar/LinuxMonitorUi.jar
APP_PARAMS=’”linuxmonitor” prod’
PID_PATH_NAME=/tmp/my_amazing-pid
echo “Starting $SERVICE_NAME ..”
if [ ! -f $PID_PATH_NAME ]; then
echo “$SERVICE_NAME tryna start..”
nohup “$PATH_TO_JAVA” $JVM_PARAMS -jar $PATH_TO_JAR $APP_PARAMS >> amazing.out 2>&1 &
echo “$SERVICE_NAME started. Writing a PID..”
echo $! > $PID_PATH_NAME
echo “$SERVICE_NAME PID has been written.”
else
echo “$SERVICE_NAME is already running..”
PID=$(cat $PID_PATH_NAME);
echo “$SERVICE_NAME with PID = $PID stopping..”
kill $PID;
echo “$SERVICE_NAME with PID = $PID stopped..”
rm $PID_PATH_NAME;
echo “file $PID_PATH_NAME was deleted”
fi