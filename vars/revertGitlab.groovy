def call() {
    echo "last id merge:${env.gitlabMergeRequestLastCommit}"

     withCredentials([string(credentialsId: 'secret-token', variable: 'PRIVATE_TOKEN')]) {  
        echo "trigger gitlab revert"
        
         sh "curl -H \"PRIVATE-TOKEN:\$PRIVATE_TOKEN\" -X POST \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/repository/commits/:${env.gitlabMergeRequestLastCommit}/revert\" --form branch=${env.gitlabTargetBranch}"
    }
}