def call () {
    def stg = [:]
    def stage_name = "${env.STAGE_NAME}"
    stg.error = "${stage_name}"
    echo "error on stage ${stg.error}"

    return stg 

}