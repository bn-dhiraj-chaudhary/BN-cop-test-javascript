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

            echo 'Pipeline completed successfully'

        } catch (Exception e) {

            echo 'Pipeline failed'
            throw e
        }
    }
}
