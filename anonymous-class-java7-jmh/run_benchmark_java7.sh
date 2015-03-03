export JAVA_HOME=/home/my/dev/java/jdk1.7.0_75
#~/dev/java/jdk1.7.0_80/
export PATH=$JAVA_HOME/bin/:$PATH
#echo $JAVA_HOME
java -version
mvn clean install && java -jar target/benchmarks.jar -gc true  | tee result_benchmark_java7_anonymous.log
