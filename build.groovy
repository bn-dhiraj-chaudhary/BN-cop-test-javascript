```groovy
pipeline {
    parameters {
        string(
            name: 'NODE',
            defaultValue: 'any',
            description: 'Jenkins node/agent label to run the pipeline on.'
        )
    }

    agent {
        label "${params.NODE}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Clean') {
            steps {
                echo 'Cleaning previous build artifacts...'
                sh 'rm -rf dist'
                sh 'rm -rf node_modules'
                sh 'npm cache clean --force'
            }
        }

        stage('Build') {
            steps {
                echo 'Installing dependencies...'
                sh 'npm ci'

                echo 'Building JavaScript application...'
                sh 'npm run build'
            }
        }

        stage('Validation') {
            parallel {

                stage('Test') {
                    steps {
                        echo 'Running tests...'
                        sh 'npm test'
                    }
                }

                stage('Lint') {
                    steps {
                        echo 'Running lint checks...'
                        sh 'npm run lint'
                    }
                }
            }
        }

        stage('Security Scan') {
            stages {

               
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully'
        }

        failure {
            echo 'Pipeline failed'
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
```
