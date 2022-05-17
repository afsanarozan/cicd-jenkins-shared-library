def call (String stage_name = 'Checkout Code') {
    //def stg = [:]
    def stage = "${stage_name}"

    echo "${stage}"
    // stg.error = "${args.stage_name}"
    // echo "error on stage ${stg.error}"

    // return stg 

}