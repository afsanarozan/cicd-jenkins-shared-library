def call() {
//   def envar = checkoutTagging()
//   def setting = settings()
    echo "Deploy to kubernetes "
    container('base'){
            dir('helm') {
                withKubeConfig([credentialsId: 'nonprod-cluster']) {
                script {
                sh """
                    kubectl config use-context do-sgp1-labs-nonproduction  
                    kubectl config get-contexts
                    helm
                """
                }
            }
        }
    }       
}
