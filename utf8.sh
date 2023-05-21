#!/bin/bash

java -Dbenchmark.utf8=true -jar target/benchmarks.jar Utf8 -rff results-utf8.csv -rf csv
gnuplot benchmark-utf8.plot

java -jar target/benchmarks.jar Utf8 -rff results-ascii.csv -rf csv
gnuplot benchmark-ascii.plot
