def call() {
  def envar = checkoutCode()
  def stage = stageName(STAGE_NAME: env.STAGE_NAME)
  
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
  } finally {
      error "This pipeline stops here!"
  }
 
        
}

