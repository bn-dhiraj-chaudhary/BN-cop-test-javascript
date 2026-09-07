pipeline {
    agent any
    environment {
        POLARIS__TOKEN = credentials('POLARIS_TOKEN')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
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
        stage('Security Scan') {
            steps {
                echo 'Running Black Duck Polaris security scan...'
            }
        }
        stage('Polaris Black Duck Security Scan') {
            steps {
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
        
    }

    post {
        success {
            echo 'Pipeline completed successfully'
        }

        failure {
            echo 'Pipeline failed'
        }
    }
}
