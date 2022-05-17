def call() {
  stageName(nameStage: env.STAGE_NAME)  
  def envar = checkoutCode()
  print("ini :" + envar)
    if (envar.branch == '*/development'){
        echo "passed"
    } else {
        echo "job success"
        error "This pipeline stops here!"
    }
        
}

