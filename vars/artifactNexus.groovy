def call(Map Args) {
    container('base') {
    echo "checkout branch main" 
        checkout([$class: 'GitSCM', branches: [[name: "main"]], userRemoteConfigs: [[credentialsId: "6c8b6848-ca10-4862-b1a9-fe3e6d46da61", url: "https://gitlab.com/afsanarozanaufal/helm-chart"]]])
        sh 'ls'
        installPlugin()
        sh """
        chmod 777 ./package.sh
        ./package.sh
        ls
        """
    }
}

def installPlugin(Map Args){
    sh """
        helm plugin install --version master https://github.com/sonatype-nexus-community/helm-nexus-push.git
        helm repo add helm-private-repo https://artifactory.finterlabs.com/repository/finterlabs-helm-local/ --username admin --password @klik123
    """
}