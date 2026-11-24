# PDC CI/CD Pipeline: GitHub -> Jenkins (AWS EC2) -> Kaggle GPU

Every push to GitHub triggers a webhook. Jenkins on an AWS EC2 machine then
pulls the code and data, compiles the Java program, tests it, runs it on the
data, and saves the report. An optional stage sends a GPU job to Kaggle.

| Folder | What it holds |
|---|---|
| `src/` | Java source code (`MarksAnalyzer.java`) |
| `data/` | Input data (`marks.csv`) |
| `tests/` | Test data, expected output, test script |
| `kaggle/` | GPU job + Kaggle metadata (optional stage) |
| `Jenkinsfile` | The pipeline definition |

Run locally:
```
javac -d build src/MarksAnalyzer.java
sh tests/run_tests.sh
java -cp build MarksAnalyzer data/marks.csv results/report.txt
```
