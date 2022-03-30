def call(Map args){
    def config = pipelineCfg()
    def envar = checkoutCode()
    sh "echo check branch"
    if(envar.environment == 'dev'){
        echo "Next"
    } else {
        notification()
    }
}
