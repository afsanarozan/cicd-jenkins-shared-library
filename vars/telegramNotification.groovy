def call(String buildStatus = 'STARTED') {
    def config = pipelineCfg()

    def telegram_chatid = -784775712
    def telegram_url    = "https://api.telegram.org/bot5117336515:AAFGksphWynQnpMlsF9dbqruHgFGRiM9-pw/sendMessage"
    def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
    def summary = "${subject} (${env.BUILD_URL})"

    echo "Job Success"
    container('curl'){  
        if (buildStatus == 'STARTED') {
            notificationsStarted(subject: subject, summary: summary, telegram_chatid: telegram_chatid, telegram_url:telegram_url)
        } else if (buildStatus == 'SUCCESS') {
            notifications(telegram_url: config.telegram_url, telegram_chatid: config.telegram_chatid, job: env.JOB_NAME, job_numb: env.BUILD_NUMBER, job_url: env.BUILD_URL)
        } else {
            echo "passed"
        }
        // notifications(telegram_url: config.telegram_url, telegram_chatid: config.telegram_chatid, job: env.JOB_NAME, job_numb: env.BUILD_NUMBER, job_url: env.BUILD_URL)
        // echo "${env.BUILD_URL}"
    }
}

def notificationsStarted (Map args) {
    def message = "${args.summary}"
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
