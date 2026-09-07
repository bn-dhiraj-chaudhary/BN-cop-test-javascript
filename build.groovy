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

        stage('Build & Test') {
            parallel {

                stage('Build') {
                    steps {
                        echo 'Building Azure Repos project...'
                    }
                }

                stage('Test') {
                    steps {
                        echo 'Running tests...'
                    }
                }
            }
        }

        stage('Security Scan') {
            stages {

                stage('Security Scan Result') {
                    steps {
                        echo 'Polaris security scan completed.'
                    }
                }
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
