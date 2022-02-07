def call() {
  def config = pipelineCfg()
  def envar = [:]
  sh "printenv | sort"
  
  switch(env.BRANCH_NAME) {
  
    case 'main':
      if (env.gitlabActionType == 'NOTE' && env.gitlabTriggerPhrase == 'approved' ) {
        echo "apalah"
        envar.branch      = "main"
      }
      break;
   case 'dev': 
     if (gitlabActionType == 'TAG_PUSH') {
        echo "apalah"
        envar.branch      = "dev"
      }
      break;
    case 'master':
      if (gitlabActionType == 'PUSH') {
        echo "apalah"
        envar.branch      = "master"
      }
      break; 
    case 'sit':
      if (gitlabActionType == 'MERGE_REQUEST') {
        echo "apalah"
        envar.branch      = "sit"
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

def check_approver(email)
{
   def approval = "adekurniawan1999"
   if(approval.contains(email))
    {
      echo "email masuk"
      return 1}
}
