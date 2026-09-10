properties([
    parameters([
        string(
            name: 'NODE',
            defaultValue: 'any',
            description: 'Jenkins node/agent label to run the pipeline on.'
        )
    ])
])

node(params.NODE) {

    withCredentials([
        string(
            credentialsId: 'POLARIS_TOKEN',
            variable: 'POLARIS__TOKEN'
        )
    ]) {

        try {

            stage('Checkout') {
                checkout scm
            }

            stage('Build') {
                echo 'Building Azure Repos project...'
            }

            stage('Test') {
                echo 'Running tests...'
            }

            stage('Security Scan') {
                echo 'Running Black Duck Polaris security scan...'
            }

            stage('Polaris Black Duck Security Scan') {
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

            echo 'Pipeline completed successfully'

        } catch (Exception e) {

            echo 'Pipeline failed'
            throw e
        }
    }
}
