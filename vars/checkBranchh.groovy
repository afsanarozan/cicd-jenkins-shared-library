def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  print("ini :" + envar)
    if (envar.branch == '*/developmt'){
        echo "passed"
    } else {
        echo "job success"
        error "This pipeline stops here!"
    }
        
}

