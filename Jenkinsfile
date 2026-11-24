pipeline {
    agent any

    parameters {
        booleanParam(name: 'RUN_ON_KAGGLE', defaultValue: false,
                     description: 'Also send the GPU job to Kaggle')
    }

    triggers {
        githubPush()            // build automatically when GitHub webhook fires
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm    // pull code + data from GitHub
            }
        }
        stage('Build') {
            steps {
                sh 'rm -rf build results && mkdir -p build results'
                sh 'javac -d build MarksAnalyzer.java'
            }
        }
        stage('Test') {
            steps {
                sh 'sh run_tests.sh'
            }
        }
        stage('Run') {
            steps {
                sh 'java -cp build MarksAnalyzer marks.csv results/report.txt'
                sh 'cat results/report.txt'
            }
        }
        stage('Kaggle GPU Job') {
            when { expression { params.RUN_ON_KAGGLE } }
            steps {
                withCredentials([file(credentialsId: 'kaggle-json', variable: 'KAGGLE_JSON')]) {
                    sh '''
                        python3 -m venv .venv
                        .venv/bin/pip install -q kaggle
                        mkdir -p ~/.kaggle
                        cp "$KAGGLE_JSON" ~/.kaggle/kaggle.json
                        chmod 600 ~/.kaggle/kaggle.json
                        .venv/bin/kaggle kernels push -p .
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'results/*.txt', allowEmptyArchive: true
        }
        success { echo 'Pipeline finished successfully' }
        failure { echo 'Pipeline failed - check the stage logs' }
    }
}
