def call(Map var) {
    echo "Job Success"
    sh "apt-get install curl"
    notifications(telegram_url: var.telegram_url, telegram_chatid: var.telegram_chatid, job: var.job, job_numb: var.job_numb, job_url: var.job_url)
    echo "${env.BUILD_URL}"
}


def notifications(Map args) {
    def message = "CICD Pipeline ${args.job} with build ${args.job_numb} \n\n More info at: ${args.job_url} \n\n Unit Test: passed \n\n Total Time :"
    parallel(
        "Telegram": {
            sh "curl -s -X POST ${args.telegram_url} -d chat_id=${args.telegram_chatid} -d text='${message}'"
        },
        "Jira": {
            //jiraSend color: "${args.jira_url}", message: "${message}", channel: "${args.slack_channel}"
        }
    )
}