def call (Map args) {
    //def stg = [:]
    def stage = "${args.stage_nam}"

    echo "${stage}"
    // stg.error = "${args.stage_name}"
    // echo "error on stage ${stg.error}"

    // return stg 

}