def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  
  sh "printenv | sort"
  envar.tag = sh(script: 'git tag | tail -1 ', returnStdout: true)
  echo "test ${envar.tag}"

switch(envar.version) {
    case 'release':
      DOcredential = config.credential_prod_demo
      context   = config.DO_production_cluster
      namespace  = config.name_space_release
      env = "release"
      values = "values-release.yaml"
      break;
    case 'release-demo':
      DOcredential = config.credential_prod_demo
      context   = config.DO_production_cluster
      namespace  = config.name_space_release
      env = "release"
      values = "values-demo.yaml"
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
                        helmnonprod(service_name: config.service_name, name_space: namespace)
                    }
                    if(envar.environment  == 'production'){
                        helmprod(service_name: config.service_name, name_space: namespace, dstVersion: envar.tag)
                    }
                }
    }       
}

def helmnonprod(Map args) {
    sh """
    ls
    kubectl get ns
    helm upgrade ${args.service_name} --install Charts/${args.service_name} -f ${values} -n ${args.name_space} --set image.tag=${env}-${BUILD_NUMBER} --create-namespace
    """
}

def helmprod(Map args) {
    sh """
    ls
    kubectl get ns
    helm upgrade ${args.service_name} --install Charts/${args.service_name} -f ${values} -n ${args.name_space} --set image.tag=${args.dstVersion} --create-namespace
    """
}

