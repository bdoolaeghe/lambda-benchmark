export JAVA_HOME=/cygdrive/c/HOMEWARE/ITEC-Toolbox/apps/jdk/jdk1.8.0_u31/
export PATH=$JAVA_HOME/bin:$PATH
mvn clean install
java -jar target/benchmarks.jar -v EXTRA -gc true  