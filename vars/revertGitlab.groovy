def call() {
    echo "last id merge:${env.gitlabMergeRequestLastCommit}"

container('curl') {
     withCredentials([usernamePassword(credentialsId: 'gitlab-auth-token', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        echo "trigger gitlab revert"
        
         sh "curl -H \"\$USERNAME:\$PASSWORD\" -X POST \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/repository/commits/:${env.gitlabMergeRequestLastCommit}/revert\" --form branch=${env.gitlabTargetBranch}"
    }
}
}