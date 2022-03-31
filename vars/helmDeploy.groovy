def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  
  sh "printenv | sort"

switch(envar.version) {
    case 'release':
      DOcredential = config.credential_prod_monitoring
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
                    if(envar.branch == '*/development' || envar.environment  == 'staging') {
                        helmUpgrade(service_name: config.service_name, name_space: namespace)
                    }
                    if(envar.environment  == 'production'){
                        helmInstall(service_name: config.service_name, name_space: namespace, dstVersion: "${config.Tag}-${BUILD_NUMBER}")
                    }
                }
    }       
}

def helmUpgrade(Map args) {
    sh """
    ls
    kubectl get ns
    helm upgrade ${args.service_name} --install Charts/${args.service_name} -f ${values} -n ${args.name_space} --set image.tag=${env}-${BUILD_NUMBER}
    """
}

def helmInstall(Map args) {
    sh """
    ls
    kubectl get ns
    helm upgrade ${args.service_name} --install Charts/${args.service_name} -f ${values} -n ${args.name_space} --set image.tag=${args.dstVersion}
    """
}

