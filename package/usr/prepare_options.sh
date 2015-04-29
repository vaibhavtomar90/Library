. /lib/lsb/init-functions

DEBUG_PORT=37305
JMX_PORT=37310

JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote
    -Dcom.sun.management.jmxremote.ssl=false
    -Dcom.sun.management.jmxremote.authenticate=false
    -Dcom.sun.management.jmxremote.port=$JMX_PORT"
JAVA_OPTS="-Xmx8G -Xms8G
           -XX:+PrintCommandLineFlags
           $JAVA_OPTS"
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:MaxGCPauseMillis=10 -Xloggc:${LOG_DIR}/`date +%Y_%m_%d-%H%M_gc`.log"

boot_opts="$cmd_opts"

echo "Boot opts is '$boot_opts'"

if [ -z $boot_opts ]; then
    JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=$DEBUG_PORT"
elif [ $boot_opts == 'sus' ]; then
    JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=$DEBUG_PORT"
fi


cd $PRICING_BASE
