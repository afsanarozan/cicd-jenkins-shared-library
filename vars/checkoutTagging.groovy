def call() {
  
  def config = pipelineCfg()
  def envar = [:]
  def setting = settings()
  def username
  sh "echo ${config.application_name}"
  token = setting.tokenapi
  sh "printenv | sort"
//  sh "curl --request GET --header 'PRIVATE-TOKEN: ${token}' https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/merge_requests/${env.gitlabMergeRequestIid}?state=opened"

 if(env.BRANCH_NAME == 'master' && env.gitlabTriggerPhrase == 'approved'){
  sh "curl -H \"PRIVATE-TOKEN: ${token}\" \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/merge_requests/${env.gitlabMergeRequestIid}?state=opened\" --output resultMerge.json"

  def jsonRs = readJSON file: "resultMerge.json"
  echo "Username : ${jsonRs.author.username}"
  username="${jsonRs.author.username}"
 }

  switch(env.BRANCH_NAME) {
    case 'master':
      if (env.gitlabActionType == 'NOTE' && env.gitlabTriggerPhrase == 'approved' && (check_approver(username) == 1)) {
        echo "apalah"
        envar.environment = 'staging'
        envar.version     = "beta"
        envar.branch      = "master"
      }else if(gitlabActionType == 'TAG_PUSH') {
        envar.environment = 'production'
        envar.version     = "release"
        envar.branch      = env.gitlabSourceBranch
      }
      break; 
    case 'bss': 
      envar.environment = 'staging'
      envar.version     = "bss"
      envar.branch      = "bss"
      break; 
    case 'sit': 
      envar.environment = 'sit'
      envar.version     = "beta"
      envar.branch      = "sit"
      break; 
    default: 
      envar.environment = 'dev'
      envar.branch      = "*/${env.BRANCH_NAME}"
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
   def approval = "ysetiawan, support.axisnet, rozaqabdul678, riowiraldhani"
   if(approval.contains(email))
    {
      echo "email masuk"
      return 1}
}
