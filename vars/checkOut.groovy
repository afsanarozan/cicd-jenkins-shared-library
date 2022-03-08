/* def call(Map envar) {
    echo "checkout branch ${envar.branch}"
    checkout changelog: true, poll: true, scm: [
      $class: 'GitSCM',
      branches: [[name: "${envar.branch}"]],
      doGenerateSubmoduleConfigurations: false,
      submoduleCfg: [],
      userRemoteConfigs: [[credentialsId: 'repository-access', url: "${envar.git_url}"]]
    ]
}
 */
//sample merged from jenkins syntax example (step checkout): https://jenkins-dev.toolchain.klik.digital/job/Development/job/RnD/job/Golang_Application/pipeline-syntax/ 
//checkout([
//  $class: 'GitSCM', 
//  branches: [[name: '*/main'], 
//  [name: '*/development']], 
//  extensions: [[$class: 'PreBuildMerge', options: [mergeRemote: 'development', mergeTarget: 'main']]],
//   userRemoteConfigs: [[credentialsId: 'repository-access', url: 'https://gitlab.com/kds-platform/devsecops/golang-apps.git']]])
def call(Map args) {
    def config = pipelineCfg()
    // def ext = []
    // def branches = []

    echo "checkout branch ${config.branch}" 
    // checkout([$class: 'GitSCM', branches: [[name: "${config.branch}"]], userRemoteConfigs: [[credentialsId: "${config.credential}", url: "https://gitlab.com/kliklab/automation-platform/services-platform/${config.name_space}/${config.service_name}.git"]]])
    checkout([$class: 'GitSCM', branches: [[name: "${config.branch}"]], userRemoteConfigs: [[credentialsId: "${config.credential}", url: "https://gitlab.com/kliklab/api-management-platform/api-gateway/reporting-service"]]])
    sh 'ls'
}

