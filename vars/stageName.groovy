def call (String stage) {
    def stg = [:]

    stg.error = "${stage}"
    echo "error on stage ${stg.error}"

    return stg 

}