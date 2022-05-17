def call (String test) {
    Map stage_name = "${test}"
    echo "error on stage ${test}"
    echo "error on stage ${stage_name}"

    return stage_name
}