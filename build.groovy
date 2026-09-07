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
    }
    parameters {
    string(
        name: 'NODE',
        defaultValue: 'any',
        description: 'Jenkins node/agent label to run the pipeline on.'
    )
}

    nodes {
        node {
            label "${params.NODE}"
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
