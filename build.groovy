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
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            stages {

                stage('Install Dependencies') {
                    steps {
                        sh 'npm ci'
                    }
                }

                stage('Build') {
                    steps {
                        echo 'Building JavaScript project...'
                        sh 'npm run build'
                    }
                }

                stage('Test & Quality') {
                    parallel {

                        stage('Unit Tests') {
                            steps {
                                echo 'Running unit tests...'
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
            }
        }

        stage('Security Scan') {
            stages {

                stage('Polaris Black Duck Security Scan') {
                    steps {
                        echo 'Running Black Duck Polaris security scan...'

                        security_scan(
                            product: 'polaris',
                            polaris_server_url: POLARIS_URL,
                            polaris_access_token: POLARIS__TOKEN,
                            polaris_application_name: 'BN-cop-test-javascript-app',
                            polaris_project_name: 'bn-dhiraj-chaudhary/BN-cop-test-javascript',
                            polaris_branch_name: 'main',
                            polaris_assessment_types: 'SAST,SCA'
                        )
                    }
                }

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
