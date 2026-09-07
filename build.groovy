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

    environment {
        POLARIS__TOKEN = credentials('POLARIS_TOKEN')
        APP_ENV = 'ci'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Environment Setup') {
            steps {
                sh '''
                    echo "Setting up Jenkins workspace..."
                    export CI_MODE=true
                    mkdir -p workspace-data
                    mkdir -p logs
                    touch logs/pipeline.log
                    echo "Environment setup completed."
                '''
            }
        }

        stage('File Operations') {
            parallel {

                stage('Create Configuration') {
                    steps {
                        sh '''
                            echo "Creating configuration file..."
                            mkdir -p config
                            echo "environment=${APP_ENV}" > config/build.properties
                            echo "workspace=${WORKSPACE}" >> config/build.properties
                            cat config/build.properties
                        '''
                    }
                }

                stage('Collect Repository Information') {
                    steps {
                        sh '''
                            echo "Collecting repository information..."
                            pwd
                            echo "Files in workspace:"
                            ls -la
                            find . -maxdepth 2 -type f | sort
                        '''
                    }
                }

                stage('Prepare Logs') {
                    steps {
                        sh '''
                            echo "Preparing log files..."
                            mkdir -p logs
                            echo "Pipeline started at $(date)" > logs/pipeline.log
                            echo "Jenkins job: ${JOB_NAME}" >> logs/pipeline.log
                            echo "Build number: ${BUILD_NUMBER}" >> logs/pipeline.log
                            cat logs/pipeline.log
                        '''
                    }
                }
            }
        }

        stage('Workspace Cleanup') {
            steps {
                sh '''
                    echo "Removing temporary files..."
                    rm -rf workspace-data
                    echo "Temporary workspace data removed."
                '''
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
            sh '''
                echo "Final workspace contents:"
                ls -la
            '''
            echo 'Pipeline execution finished.'
        }
    }
}
```
