def call() {
    echo "last id merge:${env.gitlabMergeRequestLastCommit}"

    //  withCredentials([string(credentialsId: 'secret-token', variable: 'PRIVATE_TOKEN')]) {  
     withCredentials([usernamePassword(credentialsId: 'gitlab-auth-token', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        echo "trigger gitlab revert"
        
         sh "curl -H \"\$USERNAME:\$PASSWORD\" -X POST \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/repository/commits/:${env.gitlabMergeRequestLastCommit}/revert\" --form branch=${env.gitlabTargetBranch}"
    }
}