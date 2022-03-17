 def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  
sh "printenv | sort"

switch(envar.version) {
    case 'release':
      DOcredential = config.credential_prod
      context   = config.DO_production_cluster
      namespace  = config.name_space_release
      env = "release"
      values = "values-release.yaml"
      break;
    case 'beta':
      DOcredential = config.credential
      context   = config.DO_nonprod_cluster
      namespace  = config.name_space_beta
      env = "beta"
      values = "values-beta.yaml"
      break;
    case 'alpha':
      DOcredential = config.credential
      context   = config.DO_nonprod_cluster
      namespace  = config.name_space_alpha
      env = "alpha"
      values = "values-alpha.yaml"
      break;
    default: 
      sh "exit 1"
      break;
}

container('base'){
                    withKubeConfig([credentialsId: DOcredential]) {
                    try {
                        helmUpgrade(service_name: config.service_name, name_space: namespace)
                    } catch (e) {
                        helmInstall(service_name: config.service_name, name_space: namespace)
                    }
                }
    }       
}

def helmUpgrade(Map args) {
    sh """
    ls
    echo ${values}
    helm upgrade ${args.service_name} Charts/${args.service_name} -f ${values} -n ${args.name_space}
    """
}

def helmInstall(Map args) {
    sh """
    ls
    helm install ${args.service_name} Charts/${args.service_name} -f ${values} -n ${args.name_space}
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

