def call() {
  def envar = checkoutCode()
  def test = [:]

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
      test.testing = "${env.STAGE_NAME}"
      echo "${test.testing}"
      return test
  } finally {
      error "This pipeline stops here!"
  }
 
        
}

