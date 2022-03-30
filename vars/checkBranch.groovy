def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  print("ini :" + envar)
    if (env.gitlabSourceBranch == 'development'){
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
