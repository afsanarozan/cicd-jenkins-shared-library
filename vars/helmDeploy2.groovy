def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  
sh "printenv | sort"

switch(envar.version) {
    case 'release':
      DOcredential = config.credential_prod
      context   = config.DO_production_cluster
      namespace  = "ehrm"
      env = "release"
      break;
    case 'beta':
      DOcredential = config.credential
      context   = config.DO_nonprod_cluster
      namespace  = "ehrm"
      env = "beta"
      break;
    case 'alpha':
      DOcredential = config.credential_staging
      context   = config.DO_staging_cluster
      namespace  = "ehrm"
      env = "alpha"
      break;
    default: 
      sh "exit 1"
      break;
}

container('base'){
            dir('Charts') {
                    withKubeConfig([credentialsId: 'nonprod-cluster']) {
                    try {
                        helmUpgrade(service_name: config.service_name, name_space: config.name_space)
                    } catch (e) {
                        helmInstall(service_name: config.service_name, name_space: config.name_space)
                    }
                }
            }
    }       
}

def helmUpgrade(Map args) {
    sh """
    cd ${args.service_name}
    helm upgrade ${args.service_name} . -f values.yaml -n ${args.name_space}
    """
}

def helmInstall(Map args) {
    sh "cd ${args.service_name}"
    sh "helm install ${args.service_name} . -f values.yaml -n ${args.name_space}"
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

