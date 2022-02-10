def call(Map var) {
    def config = pipelineCfg()
    echo "Job Success"
    container('curl'){
        notifications(telegram_url: config.telegram_url, telegram_chatid: config.telegram_chatid, job: env.JOB_NAME, job_numb: env.JOB_NUMB, job_url: env.BUILD_URL, job_status: config.job_status)
        echo "${env.BUILD_URL}"
    }
}


def notifications(Map args) {
    def message = "CICD Pipeline ${args.job} ${args.job_status} with build ${args.job_numb} \n\n More info at: ${args.job_url} \n\n Unit Test: passed \n\n Total Time : ${currentBuild.durationString}"
    parallel(
        "Telegram": {
            sh "curl -s -X POST ${args.telegram_url} -d chat_id=${args.telegram_chatid} -d text='${message}'"
        },
        "Jira": {
            //jiraSend color: "${args.jira_url}", message: "${message}", channel: "${args.slack_channel}"
        }
    )
}