#!/usr/bin/env groovy

/**
 * Send notifications based on build status string
 */
def call(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus = buildStatus ?: 'SUCCESS'

  def root = tool type: 'go', name: 'Go'
    withEnv(["GOROOT=${root}", "PATH+GO=${root}/bin"]) {
      try {
        sh "go tool cover -func=coverage.out"
        def unitTestGetValue = sh(returnStdout: true, script: 'go tool cover -func=coverage.out | grep total | sed "s/[[:blank:]]*$//;s/.*[[:blank:]]//"')
      } catch (e) {
        def unitTestGetValue = "0.0%"
        echo "${unitTestGetValue}"
      } finally {
        // Default values
        def colorName = 'RED'
        def colorCode = '#FF0000'
        def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
        def summary = "${subject} (${env.BUILD_URL})"
        def details = """<p>${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
          <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""
      } 

      // Override default values based on build status
      if (buildStatus == 'STARTED') {
        color = 'YELLOW'
        colorCode = '#FFFF00'
      } else if (buildStatus == 'SUCCESS') {
        color = 'GREEN'
        colorCode = '#00FF00'
      } else {
        color = 'RED'
        colorCode = '#FF0000'
      }

      // Send notifications
      slackSend (color: colorCode, message: summary)
    }
}
