def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  def setting = settings()

sh "printenv | sort"

switch(env.BRANCH_NAME) {
    case 'master':
      DOcredential = setting.credential
      DO_cluster   = setting.DO_nonprod_cluster
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

