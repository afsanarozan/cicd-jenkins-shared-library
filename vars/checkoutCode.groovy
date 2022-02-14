def call() {
  def config = pipelineCfg()
  def envar = [:]
  sh "printenv | sort"
  
  switch(env.BRANCH_NAME) {
  
    case 'staging':
      if (gitlabActionType == 'PUSH') {
        envar.branch      = "staging"
        envar.environment = 'staging'
        envar.version     = "beta"
      }
      break;
   case 'master': 
     if (gitlabActionType == 'TAG_PUSH') {
        envar.branch      = "master"
        envar.environment = 'production'
        envar.version     = "release"
      }
      break;
    case 'dev':
      if (gitlabActionType == 'PUSH') {
        envar.branch      = "dev"
        envar.environment = 'dev'
        envar.version     = "alpha"
      }
      break;  
  }

  checkout_code(config, envar.branch)
  
  return envar
  
}
def checkout_code (config, branch) {
    echo "checkout branch ${branch}"
    checkout changelog: true, poll: true, scm: [
      $class: 'GitSCM',
      branches: [[name: "${branch}"]],
      doGenerateSubmoduleConfigurations: false,
      submoduleCfg: [],
      userRemoteConfigs: [[credentialsId: 'gitlab-auth-token', url: "${config.git_url}"]]
    ]
}

