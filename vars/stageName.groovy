def call (Map args) {
    def stg = [:]

    stg.error = "${args.STAGE_NAME}"
    echo "error on stage ${stg.error}"

    return stg 

}