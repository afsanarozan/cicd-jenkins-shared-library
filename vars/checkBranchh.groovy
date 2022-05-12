def call() {
  def envar = checkoutCode()
  def env = [:]

  try {
    print("ini :" + envar)
    if (envar.branch == '*/development'){
        echo "passed"
    } else {
        echo "job success"
        error "This pipeline stops here!"
    }
  } catch (e) {
      echo "${env.STAGE_NAME}"
      return env
  }
 
        
}

