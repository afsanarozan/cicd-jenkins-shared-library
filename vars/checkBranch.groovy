def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  print("ini :" + envar)
    if (env.BRANCH_NAME == 'development'){
        echo "passed"
    } else {
        echo "job success"
        skip()
        error "This pipeline stops here!"
    }
        
}

def skip(){
    def envar = slackNotification()
}
