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
def add(branches, credential, url) {
    // def ext = []
    // def branches = []

    echo "checkout branch ${branches}"
    
    if  (branches == "null") {
        error "branch is null"
    }  
    // branches.add([name: "${envar.branch}"])
    // sh "git config --global pager.branch false"

    // echo "target branch ${envar.target_branch}"
    //  if  ((envar.target_branch != "null") && (envar.target_branch != envar.branch))  {
        
    //     branches.add([name: "${envar.target_branch}"])
    //     ext.add(['$class': 'PreBuildMerge', options: [mergeRemote: "origin", mergeTarget: "${envar.branch}"]])

    //     sh "git config --global user.email ${env.gitlabUserEmail}"
    //     sh "git config --global user.name ${env.gitlabUserName}"
    // }  

    // checkout changelog: true, poll: true, scm: [
    //   $class: 'GitSCM',
    //   branches: branches,
    //   extensions: ext,
    //   userRemoteConfigs: [[credentialsId: 'gitlab-auth-token', url: "${envar.git_url}"]]
    // ]
    // sh "git config --global pager.branch false"
    // sh "git branch"
    // sh "git status"
    // sh "git --no-pager log --decorate=short --pretty=oneline -n2"
    // //sh "cat pipeline.yaml"
    // sh "cat Jenkinsfile"
    checkout([$class: 'GitSCM', branches: [[name: "${branches}"]], userRemoteConfigs: [[credentialsId: "${credential}", url: "${url}"]]])
    sh 'ls'
}

