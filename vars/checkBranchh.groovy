def call() {
  def envar = checkoutCode()

  try {
    error "This pipeline stops here!"
    // print("ini :" + envar)
    // if (envar.branch == '*/devopment'){
    //     echo "passed"
    // } 
    // else {
    //     echo "job success"
    //     error "This pipeline stops here!"
    // }
  } catch (e) {
      echo "This is Stage : ${env.STAGE_NAME}"
      return env.STAGE_NAME
  } finally {
      error "This pipeline stops here!"
  }
 
        
}

