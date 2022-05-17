def call (Map args) {
    def stg = [:]

    echo "${stg}"
    stg.error = "${args.stage_name}"
    echo "error on stage ${stg.error}"

    return stg 

}