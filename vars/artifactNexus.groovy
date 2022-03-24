def call(Map Args) {
    container('base') {
    echo "checkout branch main" 
        checkout([$class: 'GitSCM', branches: [[name: "main"]], userRemoteConfigs: [[credentialsId: "6c8b6848-ca10-4862-b1a9-fe3e6d46da61", url: "https://gitlab.com/afsanarozanaufal/helm-chart"]]])
        sh 'ls'
        sh """
        helm plugin install --version master https://github.com/sonatype-nexus-community/helm-nexus-push.git
        chmod 777 ./package.sh
        ./package.sh
        ls
        """
    }
}