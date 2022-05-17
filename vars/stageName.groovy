def call (String stage_name = 'Checkout Code') {
    def stg = [:]
    stg.error = "${stage_name}"
    echo "error on stage ${stg.error}"

    return stg 

}