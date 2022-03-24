def call(Map args){
    def config = pipelineCfg()
    container ('golang'){
        checkout changelog: true, poll: true, scm: [
        $class: 'GitSCM',
        branches: [[name: "binary-build-deployment"]],
        doGenerateSubmoduleConfigurations: false,
        submoduleCfg: [],
        userRemoteConfigs: [[credentialsId: '6c8b6848-ca10-4862-b1a9-fe3e6d46da61', url: "${config.git_url}"]]
        ]
        sh 'ls'
    }
    // script {
    //     sshPublisher(
    //         publishers: [
    //             sshPublisherDesc(
    //                 configName: 'vm-do-testing',
    //                 verbose: true,
    //                 transfers: [
    //                     sshTransfer(
    //                         remoteDirectory: "/home",
    //                         execCommand: """
    //                         export PATH=$PATH:/usr/local/go/bin;
    //                         export AWS_ACCESS_KEY_ID=YFRP3PS4LIJEOZVRUMMK;
    //                         export AWS_SECRET_ACCESS_KEY=0s4FQ470cF9AGDg7old5fLyvvhbhnqO99ooruvQdVOs;
    //                         export AWS_DEFAULT_REGION=sgp1;

    //                         git clone https://automation:glpat-Vf6rMnhFzEbshHrj2TYQ@gitlab.com/kliklab/automation-platform/services-platform/example-service.git;
    //                         cd example-service;
    //                         git checkout binary-build-deployment;
    //                         go version;

    //                         go mod download;
    //                         go mod verify;
    //                         go mod tidy -v; 
    //                         go build -o ${config.service_name}-${config.tag};

    //                         aws s3 cp ${config.service_name} --endpoint-url ${config.spaces_url} s3://binary-build/beta
    //                         """,
    //                         execTimeout: 60000  
    //                     )
    //                 ]
    //             )
    //         ]
    //     )
    // }
    sh "echo binary build"
}


def pushChart(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://helm-charts/${args.namespace}/beta/"
}


// go mod download
// go mod verify
// go mod tidy -v 