def call (Map args) {
    sh "echo stage_name : ${args.nameStage} > stageName.yaml"
}