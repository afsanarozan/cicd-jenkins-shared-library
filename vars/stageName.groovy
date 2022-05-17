def call (Map args) {
    def stg = [:]

    echo "${args.stage_name}"
    stg.error = "${args.stage_name}"
    echo "error on stage ${stg.error}"

    return stg 

}