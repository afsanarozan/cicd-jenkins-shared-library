def call(){
def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
]
def doError = "0"

    if (doError == '1'){
        sh """
        echo "Failure :("
        error "Test failed on purpose, doError == str(1)"
        slackSend channel: '#jenkins',
            color: COLOR_MAP[currentBuild.currentResult],
                message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
        """
    }else if (doError == '0'){
        sh """
        echo "Succes :"
        slackSend channel: '#jenkins',
            color: COLOR_MAP[currentBuild.currentResult],
                message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
        """
    }
}
