#!/bin/sh
# Runs the analyzer on known data and checks the output is exactly right.
set -e
java -cp build MarksAnalyzer test_data.csv results/test_output.txt
if diff -u expected.txt results/test_output.txt; then
    echo "TEST PASSED"
else
    echo "TEST FAILED"
    exit 1
fi
