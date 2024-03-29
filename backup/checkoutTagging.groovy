def call() {
  def config = pipelineCfg()
  def envar = [:]
  sh "printenv | sort"
  
  switch(env.BRANCH_NAME) {
  
    case 'main':
      if(env.gitlabActionType == 'MERGE') {
        envar.branch      = "main"
        envar.environment = 'staging'
        envar.version     = "beta"
      }else if(env.gitlabActionType == 'TAG_PUSH') {
        envar.environment = 'production'
        envar.version     = "release"
        envar.branch      = env.gitlabSourceBranch
      }
      break;
    default:
        envar.branch      = "*/${env.BRANCH_NAME}"
        envar.environment = 'dev'
        envar.version     = "alpha"
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
