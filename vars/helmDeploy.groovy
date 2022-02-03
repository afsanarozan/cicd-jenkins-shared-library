def call() {
//   def envar = checkoutTagging()
//   def setting = settings()

    stage('Deploy to Kubernetes_1') {
            container('base'){
                withKubeConfig([credentialsId: 'nonprod-cluster'])
                script {
                    sh """
                        kubectl config use-context do-sgp1-labs-nonproduction  
                        kubectl config get-contexts
                        kubectl get ns
                    """
                }
            }        
         }
    
}
