#!/usr/bin/env groovy

/**
 * Send notifications based on build status string
 */
def call(String currentBuild.result) {
  // build status of null means successful
  buildStatus = buildStatus ?: 'SUCCESS'

  // Default values
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${currentBuild.result}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"
  def details = """<p>${currentBuild.result}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""

  // Override default values based on build status
  if (currentBuild.result == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (currentBuild.result == 'SUCCESS') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else if (currentBuild.result == 'FAILED'){
    color = 'RED'
    colorCode = '#FF0000'
  }

  // Send notifications
  slackSend (color: colorCode, message: summary)
}
