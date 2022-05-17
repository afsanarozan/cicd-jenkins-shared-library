def call (Map args) {
    //def stg = [:]
    def stage = "${args.stage_name}"

    echo "${stage}"
    // stg.error = "${args.stage_name}"
    // echo "error on stage ${stg.error}"

    // return stg 

}