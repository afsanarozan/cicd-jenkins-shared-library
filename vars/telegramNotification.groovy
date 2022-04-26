def call(String buildStatus = 'STARTED') {
    def config = pipelineCfg()

    def telegram_chatid = -784775712
    def telegram_url    = "https://api.telegram.org/bot5117336515:AAFGksphWynQnpMlsF9dbqruHgFGRiM9-pw/sendMessage"

    echo "Job Success"
    container('curl'){  
        if (buildStatus == 'STARTED') {
            notificationsStarted(buildStatus: buildStatus, telegram_chatid: telegram_chatid, telegram_url:telegram_url, JOB_NAME:env.JOB_NAME, BUILD_NUMBER:env.BUILD_NUMBER, BUILD_URL:env.BUILD_URL)
            echo "${buildStatus}"
        } else if (buildStatus == 'SUCCESS') {
            notifications(telegram_url: config.telegram_url, telegram_chatid: config.telegram_chatid, job: env.JOB_NAME, job_numb: env.BUILD_NUMBER, job_url: env.BUILD_URL)
            echo "${buildStatus}"
        } else if (buildStatus == 'FAILURE'){
            echo "passed"
            echo "${buildStatus}"
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
    def message = "CICD Pipeline ${args.job} SUCCESS with build ${args.job_numb} \n\n More info at: ${args.job_url} \n\n Unit Test: passed \n\n Total Time : ${currentBuild.durationString}"
    parallel(
        "Telegram": {
            sh "curl -s -X POST ${args.telegram_url} -d chat_id=${args.telegram_chatid} -d text='${message}'"
        },
        "Jira": {
            //jiraSend color: "${args.jira_url}", message: "${message}", channel: "${args.slack_channel}"
        }
    )
}
