def call(String buildStatus, String score) {
    // build status of null means successful
    buildStatus = buildStatus ?: 'SUCCESS'
    sh "ls -la"

    def config = pipelineCfg()

    def telegram_chatid = -784775712
    def telegram_url    = "https://api.telegram.org/bot5117336515:AAFGksphWynQnpMlsF9dbqruHgFGRiM9-pw/sendMessage"

    echo "Job Success"
    echo "${buildStatus}"
    container('curl'){  
        if (buildStatus == 'STARTED') {
            notificationsStarted(buildStatus: buildStatus, telegram_chatid: telegram_chatid, telegram_url:telegram_url, JOB_NAME:env.JOB_NAME, BUILD_NUMBER:env.BUILD_NUMBER, BUILD_URL:env.BUILD_URL)
        } else if (buildStatus == 'SUCCESS'){
            echo "${buildStatus}"
            def root = tool type: 'go', name: 'Go'
            withEnv(["GOROOT=${root}", "PATH+GO=${root}/bin"]) {
                def unitTestGetValue = sh(returnStdout: true, script: 'go tool cover -func=coverage.out | grep total | sed "s/[[:blank:]]*$//;s/.*[[:blank:]]//"')
                def unitTest_score   = "Your score is ${unitTestGetValue}"
                echo "${unitTest_score}" 
            }
            sh "cat coverage.out"
            notifications(buildStatus: buildStatus, telegram_chatid: telegram_chatid, telegram_url:telegram_url, JOB_NAME:env.JOB_NAME, BUILD_NUMBER:env.BUILD_NUMBER, BUILD_URL:env.BUILD_URL, score:score)
        }   else {
            notifications(buildStatus: buildStatus, telegram_chatid: telegram_chatid, telegram_url:telegram_url, JOB_NAME:env.JOB_NAME, BUILD_NUMBER:env.BUILD_NUMBER, BUILD_URL:env.BUILD_URL)
        }
        // notifications(telegram_url: config.telegram_url, telegram_chatid: config.telegram_chatid, job: env.JOB_NAME, job_numb: env.BUILD_NUMBER, job_url: env.BUILD_URL)
        // echo "${env.BUILD_URL}"
    }
}

def notificationsStarted (Map args) {
    def subject = "${args.buildStatus}: Job ${args.JOB_NAME} ${args.BUILD_NUMBER}"
    def message = "${subject} ${args.BUILD_URL}"

    sh "curl -s -X POST ${args.telegram_url} -d chat_id=${args.telegram_chatid} -d text='${message}'"
}
def notifications(Map args) {
    def message = "CICD Pipeline ${args.JOB_NAME} ${args.buildStatus} with build ${args.BUILD_NUMBER} \n\n More info at: ${args.BUILD_URL} \n\n Unit Test: ${args.score} \n\n Total Time : ${currentBuild.durationString}"
    
    sh "curl -s -X POST ${args.telegram_url} -d chat_id=${args.telegram_chatid} -d text='${message}'"
    // parallel(
    //     "Telegram": {
    //         sh "curl -s -X POST ${args.telegram_url} -d chat_id=${args.telegram_chatid} -d text='${message}'"
    //     },
    //     "Jira": {
    //         //jiraSend color: "${args.jira_url}", message: "${message}", channel: "${args.slack_channel}"
    //     }
    // )
}
