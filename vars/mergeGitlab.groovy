def call() {
    echo "actionType: ${env.gitlabActionType}"
    echo "projectId: ${env.gitlabMergeRequestTargetProjectId}"
    echo "MergerequestIid: ${env.gitlabMergeRequestIid}"

    if(env.gitlabActionType == "MERGE") {

    withCredentials([string(credentialsId: 'Gitlab-API-Key', variable: 'secret-token')]) {  
        echo "trigger gitlab"
        // sh "curl -H \"PRIVATE-TOKEN: ${PRIVATE_TOKEN}\" \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/merge_requests/${env.gitlabMergeRequestIid}?state=opened\" --output resultMerge.json"
         sh "curl -H \"PRIVATE-TOKEN:\$secret-token\" -X PUT \"https://gitlab.com/api/v4/projects/${env.gitlabMergeRequestTargetProjectId}/merge_requests/${env.gitlabMergeRequestIid}/merge\""
    }
    // sh "cat resultMerge.json"

    }
}
