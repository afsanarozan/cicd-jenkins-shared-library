def call() {
    def config = pipelineCfg()
    def envar = checkoutCode()
    def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
    def summary = "${subject} (${env.BUILD_URL})"

  print("ini :" + envar)
    if (env.gitlabSourceBranch == 'development'){
        echo "passed"
    } else {
        echo "job success"
            // Send notifications
            slackSend (color: '#00FF00', message: "tes")
        error "This pipeline stops here!"
    }       
}

def skip(){
    // def envar = slackNotification()
      // Default values
   
}
