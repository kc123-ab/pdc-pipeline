#!/bin/sh
# Runs the analyzer on known data and checks the output is exactly right.
set -e
java -cp build MarksAnalyzer tests/test_data.csv results/test_output.txt
if diff -u tests/expected.txt results/test_output.txt; then
    echo "TEST PASSED"
else
    echo "TEST FAILED"
    exit 1
fi
