def call (String stage = 'Checkout Code') {
    def stg = [:]

    stg.error = stage
    echo "error on stage ${stg.error}"

    return stg 

}