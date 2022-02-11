def call(String buildResult) {
  if ( buildResult == "SUCCESS" ) {
    def slackResponse = slackSend(channel: "jenkins")
    slackSend color: "good", message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was successful"
  }
  else if( buildResult == "FAILURE" ) {
    def slackResponse = slackSend(channel: "jenkins")
    slackSend color: "danger", message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was failed"
  }
}
