def call() {
    chartdir = sh(
        returnStdout: true,
        script: 'find * -maxdepth 2 -type d | grep -iF charts/')
        .trim().split('\r?\n')

    echo "list dir ${chartdir}"

    container('base') {
        sh 'helm plugin install --version master $NEXUS_PLUGIN'
        withCredentials(
            [usernamePassword(
            credentialsId: 'artifactory-finterlabs',
            usernameVariable: 'NEXUS_USERNAME',
            passwordVariable: 'NEXUS_PASSWORD')]) {
            for (f in chartdir) {
                lastpath = f.split('/').last()
                sh """helm repo add
                    ${lastpath} \$HELM_NEXUSREPO\\${lastpath}
                    --username \$NEXUS_USERNAME --password \$NEXUS_PASSWORD"""
                sh "helm nexus-push helm-private-repo ./${f} -u \$NEXUS_USERNAME -p \$NEXUS_PASSWORD"
            }
            }
    }
}
