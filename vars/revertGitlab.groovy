def call() {
    echo "last id merge:${env.gitlabMergeRequestLastCommit}"

container('curl') {
     withCredentials([string(credentialsId: '7890dfab-50c8-4b50-a26a-772d5a5d7917', variable: 'PRIVATE_TOKEN')]) {
        echo "trigger gitlab revert"
        
         sh "curl -H \"PRIVATE_TOKEN:\$PRIVATE_TOKEN\" -X POST \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/repository/commits/:${env.gitlabMergeRequestLastCommit}/revert\" --form branch=${env.gitlabTargetBranch}"
    }
}
}