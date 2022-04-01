def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  print("ini :" + envar)
    if (envar.branch == '*/development'){
        echo "passed"
    } else {
        echo "job success"
        error "This pipeline stops here!"
    }
        
}

