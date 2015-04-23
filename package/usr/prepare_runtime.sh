# defined in $DEFAULT)
JDK_DIRS="/usr/lib/jvm/java-8-oracle"
# Look for the right JVM to use
for jdir in $JDK_DIRS; do
    if [ -r "$jdir/bin/java" -a -z "${JAVA_HOME}" ]; then
        JAVA_HOME_TMP="$jdir"
    # checks for a real JDK like environment, needed to check if
    # really the java-gcj-compat-dev package is installed
        if [ -r "$jdir/bin/jdb" ]; then
            JAVA_HOME="$JAVA_HOME_TMP"
        fi
    fi
done

if [ -z "$JAVA_HOME" ]; then
    log_failure_msg "no JDK found - please set JAVA_HOME"
    exit 1
fi

echo "Java Home $JAVA_HOME"

set classpath
CLASSPATH=$(find ${PRICING_HOME}/libs -name '*.jar' | sort | xargs -I % echo -n "%:")
CLASSPATH=${CLASSPATH}${PRICING_HOME}/conf
echo "classpath: $CLASSPATH"


