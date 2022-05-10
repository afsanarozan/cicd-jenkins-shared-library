def call(){

    def envar = checkoutCode()
    sh "printenv | sort"

     if(envar.environment == 'staging'){
       source = "${env.gitlabSourceBranch}"
       target   = "${env.gitlabTargetBranch}"
       repo = "${env.gitlabSourceRepoHttpUrl}"
       echo "source : ${env.gitlabSourceBranch}"
       echo "target  : ${env.gitlabTargetBranch}"
       echo "repo : ${env.gitlabSourceRepoHttpUrl}"
       merge(source,target,repo)
     
     }else{
         skip()
     }

   }
    
def merge(source,target,repo){
      echo " Merge Request Detected!!  Merging"
                    checkout ([
                        $class: 'GitSCM',
                        branches: [[name: "origin/${source}"]],
                        extensions: [
                            [$class: 'CleanCheckout'],
                            [
                                $class: 'PreBuildMerge',
                                options: [
                                    fastForwardMode: 'NO_FF',
                                    mergeRemote: "origin",
                                    mergeTarget: "${target}"
                                ]
                            ],
                                     [
                                    $class: 'UserIdentity',
                                    email: 'automation@klik.digital',
                                    name: 'automation-labs'
                                ]
                        ],
                                
                        userRemoteConfigs: [[credentialsId: 'automation-auth-token', url: "${repo}"]]
                    ])


                withCredentials([usernamePassword(credentialsId: 'automation-auth-token', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh "git pull https://${USERNAME}:${PASSWORD}@${repository} main"
                    sh "git remote set-url origin https://${USERNAME}:${PASSWORD}@${repository}; git push origin HEAD:main"
   }
}

def skip(){
    sh "echo '=============== SKIP =============='"
}
