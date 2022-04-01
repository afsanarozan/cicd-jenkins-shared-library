def call() {
    echo "last id merge:${env.gitlabMergeRequestLastCommit}"

container('curl') {
     withCredentials([string(credentialsId: 'global-gitlab-rnd', variable: 'PRIVATE_TOKEN')]) {
        echo "trigger gitlab revert"
        
         sh "curl -H \"PRIVATE_TOKEN:\$PRIVATE_TOKEN\" -X POST \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/repository/commits/:${env.gitlabMergeRequestLastCommit}/revert\" --form branch=${env.gitlabTargetBranch}"
    }
}
}