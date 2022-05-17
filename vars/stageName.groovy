def call (Map args) {

    def stage_name = "${args.nameStage}"
    echo "fix iki seko kene : ${stage_name}"
    sh "echo stage_name : ${args.nameStage} > stageName.yaml"
    installCli()
    sh "ls"
    sh """ 
    yq -i '.stage_name = "${args.nameStage}"' pipeline.yaml
    cat pipeline.yaml
    """
    // def stage_name = "${test}"
    // echo "error on stage ${stage_name}" 

    // return stage_name
}

def installCli(){
    sh """
        whoami
        sudo apt-get upgrade && sudo apt-get update 
        sudo apt-get install wget -y
    """
    sh "wget -qO /usr/local/bin/yq https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64"
    sh "chmod a+x /usr/local/bin/yq"
    sh "yq --version" 

}