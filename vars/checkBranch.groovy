def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  print("ini :" + envar)
  try (env.gitlabSourceBranch == 'development'){
        echo "passed"
    } catch (e) {
        echo "job success"
        skip()
    }

//   if(envar.branch == '*/development' || envar.environment  == 'staging'){
//             container('docker') {
//                 echo "Running Docker Build"
//             }
//         }
//   if(envar.environment  == 'production'){
//             container('docker') {
//                 echo "Running Docker Build"
//             }
//         }        
}

def skip(){
    def envar = slackNotification()
}
