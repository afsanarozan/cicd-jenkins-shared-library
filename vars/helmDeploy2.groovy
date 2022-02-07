def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  
sh "printenv | sort"

switch(env.BRANCH_NAME) {
    case 'master':
      DOcredential = config.credential
      DO_cluster   = config.DO_nonprod_cluster
      namespace  = "ehrm"
      break;
    default: 
      sh "exit 1"
      break;
}

container('base'){
      withKubeConfig([credentialsId: DOcredential]) {
              sh """
                   kubectl config use-context ${DO_cluster}
                   kubectl get ns
               """        
        }
    }                       
}

