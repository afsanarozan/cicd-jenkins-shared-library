def call(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus = buildStatus ?: 'SUCCESS'
  echo "${buildStatus}"
  sh "touch stageName.yaml"
  def stg = readYaml(file: "${WORKSPACE}/stageName.yaml")

  def root = tool type: 'go', name: 'Go'
    withEnv(["GOROOT=${root}", "PATH+GO=${root}/bin"]) {

    // Override default values based on build status
      if (buildStatus == 'STARTED') {
        color = 'YELLOW'
        colorCode = '#FFFF00'
        slackSend (color: colorCode, message: "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}) ")
      } else if (buildStatus == 'SUCCESS') {
        color = 'GREEN'
        colorCode = '#00FF00'
      } else {
        color = 'RED'
        colorCode = '#FF0000'
      }

      try {
        if (fileExists('coverage.out')) {
          def unitTestGetValue = sh(returnStdout: true, script: 'go tool cover -func=coverage.out | grep total | sed "s/[[:blank:]]*$//;s/.*[[:blank:]]//"')
          slackSend (color: colorCode, message: "${buildStatus}: on stage [${stg.stage_name}], Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}) unit-test : ${unitTestGetValue}")
        } else (
          error
        )
      } catch (e) {
        def unitTestGetValue = '0.0%'
        slackSend (color: colorCode, message: "${buildStatus}: on stage [${stg.stage_name}], Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}) unit-test : ${unitTestGetValue}")
      } 
    }
}
