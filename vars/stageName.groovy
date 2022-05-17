def call (Map args) {

    def stage_name = "${args.nameStage}"
    echo "fix iki seko kene : ${stage_name}"
    sh "export name_stage=${args.nameStage}"
    sh "echo ${name_stage}"

    // def stage_name = "${test}"
    // echo "error on stage ${stage_name}" 

    // return stage_name
}