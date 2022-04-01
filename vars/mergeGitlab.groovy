def call() {
    echo "actionType: ${env.gitlabActionType}"
    echo "projectId: ${env.gitlabMergeRequestTargetProjectId}"
    echo "MergerequestIid: ${env.gitlabMergeRequestIid}"
    echo "LastCommitID :${env.gitlabMergeRequestLastCommit}"

    if(env.gitlabActionType == "MERGE") {

    withCredentials([string(credentialsId: 'secret-token', variable: 'PRIVATE_TOKEN')]) {  
        echo "trigger gitlab"
        // sh "curl -H \"PRIVATE-TOKEN: ${PRIVATE_TOKEN}\" \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/merge_requests/${env.gitlabMergeRequestIid}?state=opened\" --output resultMerge.json"
         sh "curl -H \"PRIVATE-TOKEN:\$PRIVATE_TOKEN\" -X PUT \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/merge_requests/${env.gitlabMergeRequestIid}/merge\""
    }
    // sh "cat resultMerge.json"

    }
}
