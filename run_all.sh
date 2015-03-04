#!/bin/bash


CURRENT_DIR=$(pwd)
INIT_PATH=$PATH
JAVA_7_PATH=/home/patouche/Softs/jdk/jdk1.7.0_60
JAVA_8_PATH=/home/patouche/Softs/jdk/jdk1.8.0_40
DATA_FILE="$CURRENT_DIR/personnes.txt"
SAMPLE_FILE="$CURRENT_DIR/sample.txt"
SIZE_DATA=1200000


# Datas
> "$CURRENT_DIR/personnes.txt"
lnumb=$(wc -l $SAMPLE_FILE | sed -r 's/^([0-9]+).*$/\1/g');
for (( i=1; i<=$SIZE_DATA; i=$i+$lnumb )) ; do
	cat "$SAMPLE_FILE" >> "$DATA_FILE"
done
echo "Data file $DATA_FILE contains $(wc -l $DATA_FILE | sed -r 's/^([0-9]+).*$/\1/g')"
sleep 2;


# Java 7
echo "Running for java 7";
JAVA_HOME=$JAVA_7_PATH;
PATH=$JAVA_HOME/bin:$INIT_PATH
java -version

sleep 2;
echo "Starting data compilation";
cd "$CURRENT_DIR/data"
mvn clean install

echo "Starting benchmarks"
sleep 2;
cd "$CURRENT_DIR/anonymous-class-java7-jmh"

mvn clean install \
	&& java -Dinput.data="$CURRENT_DIR/personnes.txt"  -jar target/benchmarks.jar -gc true  | tee "$CURRENT_DIR/java-7.log"


# Java 8
echo "Running for java 8";
JAVA_HOME=$JAVA_8_PATH;
PATH=$JAVA_HOME/bin:$INIT_PATH
java -version

echo "Starting data compilation";
sleep 2;
cd "$CURRENT_DIR/data"
mvn clean install

echo "Starting benchmarks"
sleep 2;
cd "$CURRENT_DIR/lambda-java8-jmh";
mvn clean install \
	&& java -Dinput.data="$CURRENT_DIR/personnes.txt"  -jar target/benchmarks.jar -gc true  | tee "$CURRENT_DIR/java-8.log"





