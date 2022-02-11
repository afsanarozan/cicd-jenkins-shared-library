credentials:
  system:
    domainCredentials:
      - credentials:
          - string:
              scope: GLOBAL
              id: jenkins-slack-integration

unclassified:
  slackNotifier:
    teamDomain: jenkins # i.e. your-company (just the workspace name not the full url)
    tokenCredentialId: hYwdoJ8mV4NnspZScZgp1Jge

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
