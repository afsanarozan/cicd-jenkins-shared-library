 def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  
  sh "printenv | sort"
  def build_number_var = "${env.BUILD_NUMBER}"

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
    sh """
    ls
    kubectl get ns
    helm upgrade ${args.service_name} --install Charts/${args.service_name} -f ${values} -n ${args.name_space} --set image.tag=${env}-${build_number_var}
    """
}


