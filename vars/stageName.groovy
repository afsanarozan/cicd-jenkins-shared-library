def call (Map args) {

    def stage_name = "${args.nameStage}"
    echo "fix iki seko kene : ${stage_name}"
    sh "echo stage_name : ${args.nameStage} > stageName.yaml"
    container('yq') {
        sh "ls"
        sh """ 
        yq -i '.stage_name = "${args.nameStage}"' pipeline.yaml
        cat pipeline.yaml
        """
    }

    // def stage_name = "${test}"
    // echo "error on stage ${stage_name}" 

    // return stage_name
}