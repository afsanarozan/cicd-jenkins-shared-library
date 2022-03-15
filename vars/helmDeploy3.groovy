 def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  
sh "printenv | sort"

switch(envar.version) {
    case 'release':
      DOcredential = config.credential_prod
      context   = config.DO_production_cluster
      namespace  = config.name_space
      env = "release"
      values = "values-beta.yaml"
      break;
    case 'beta':
      DOcredential = config.credential
      context   = config.DO_nonprod_cluster
      namespace  = config.name_space
      env = "beta"
      values = "values-beta.yaml"
      break;
    case 'alpha':
      DOcredential = config.credential
      context   = config.DO_nonprod_cluster
      namespace  = config.name_space
      env = "alpha"
      values = "values-alpha.yaml"
      break;
    default: 
      sh "exit 1"
      break;
}

container('base'){
                    withKubeConfig([credentialsId: 'nonprod-cluster']) {
                    try {
                        helmUpgrade(service_name: config.service_name, name_space: config.name_space)
                    } catch (e) {
                        helmInstall(service_name: config.service_name, name_space: config.name_space)
                    }
                }
    }       
}

def helmUpgrade(Map args) {
    sh """
    ls
    helm upgrade ${args.service_name} Charts/${args.service_name} -f ${values} -n ${args.name_space}
    """
}

def helmInstall(Map args) {
    sh """
    ls
    helm install ${args.service_name} Charts/${args.service_name} -f values.yaml -n ${args.name_space}
    """
}


//container('base'){
//      withKubeConfig([credentialsId: DOcredential]) {
//              sh """
//                  kubectl config use-context ${context}
//                   kubectl get ns
//                   kubectl get pod -n ${namespace}
//               """        
//        }
//    }                       
//}

