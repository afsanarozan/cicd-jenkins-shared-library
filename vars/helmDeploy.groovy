def call() {
  def envar = checkoutTagging()
  def setting = settings()

            container('base'){
                withKubeConfig([credentialsId: 'nonprod-cluster'])
                script {
                    sh """
                        kubectl config use-context do-sgp1-labs-nonproduction 
                        kubectl get ns
                    """
                }
            }        
}
