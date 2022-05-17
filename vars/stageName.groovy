def call (String test = "init") {
    def stage_name = "${test}"
    echo "error on stage ${stage_name}" 

    Map stage = stage_name
    echo "fix ${stage}"

    return stage
}