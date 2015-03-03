export JAVA_HOME=/home/my/dev/java/jdk1.8.0_31
#~/dev/java/jdk1.8.0_40/
export PATH=$JAVA_HOME/bin/:$PATH
#echo $JAVA_HOME
java -version
mvn clean install && java -jar target/benchmarks.jar -gc true | tee result_benchmark_java8_lambda.log
