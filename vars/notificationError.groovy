def call(Map var) {
    def config = pipelineCfg()
    echo "Job Failed"
    container('curl'){
        notifications(telegram_url: config.telegram_url, telegram_chatid: config.telegram_chatid, job: config.job, job_numb: config.job_numb, job_url: config.job_url, job_status: config.job_status)
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