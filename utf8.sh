#!/bin/bash
#CMD ["java", "-server", "-Xms4g", "-Xmx4g", "-XX:-UseBiasedLocking", "-XX:+UseStringDeduplication", "-XX:+UseNUMA", "-XX:+UseParallelGC", "-jar", "app.jar"] 

JVM_ARGS="-server -Xms2g -Xmx2g -XX:+UseStringDeduplication -XX:+UseNUMA -XX:+UseParallelGC"
java ${JVM_ARGS} \
  -Dbenchmark.utf8=true -jar target/benchmarks.jar Utf8 -rff results-utf8.csv -rf csv

./gnuplot.sh benchmark-utf8.plot

java ${JVM_ARGS} \
  -jar target/benchmarks.jar Utf8 -rff results-ascii.csv -rf csv

./gnuplot.sh benchmark-ascii.plot
