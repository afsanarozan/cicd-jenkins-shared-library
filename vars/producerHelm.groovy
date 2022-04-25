def call() {
    def chartdir = sh(
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
            sh 'helm repo add helm-private-repo $HELM_NEXUSREPO --username $NEXUS_USERNAME --password $NEXUS_PASSWORD'
            }

        for (f in chartdir) {
            sh "helm nexus-push helm-private-repo ./${f}"
        }
     }
}
