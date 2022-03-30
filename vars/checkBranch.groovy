def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  print("ini :" + envar)
  if(envar.branch == '*/development' || envar.environment  == 'staging'){
                    container('docker') {
                        echo "Running Docker Build"
                    }
          }
  if(envar.environment  == 'production'){
                    container('docker') {
                        echo "Running Docker Build"
                    }
          }else{
            skip()          
          }        
}

def skip(){
    sh "error 'This pipeline stops here!'"
}
