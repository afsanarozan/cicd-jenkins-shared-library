def call(Map args){
    def config = pipelineCfg()
    script {
        sshPublisher(
            publishers: [
                sshPublisherDesc(
                    configName: 'vm-do-testing',
                    verbose: true,
                    transfers: [
                        sshTransfer(
                            remoteDirectory: "/home",
                            execCommand: """
                            export PATH=$PATH:/usr/local/go/bin;
                            git clone https://automation:glpat-Vf6rMnhFzEbshHrj2TYQ@gitlab.com/kliklab/automation-platform/services-platform/example-service.git;
                            cd example-service;
                            git checkout binary-build-deployment;
                            go version;
                            go mod download;
                            go mod verify;
                            go mod tidy -v; 
                            go build .;
                            aws s3 cp ${config.service_name} --endpoint-url ${config.spaces_url} s3://binary-build/
                            """,
                            execTimeout: 60000
                        )
                    ]
                )
            ]
        )
    }
    sh "echo binary build"
}

// def sshDeploy(Map args){
//     sshCommand remote: remote, command: "ls -lrt"
// }

// def goEnv(){
//     sh '''
//     export PATH=$PATH:/usr/local/go/bin
//     git clone https://automation:glpat-Vf6rMnhFzEbshHrj2TYQ@gitlab.com/kliklab/automation-platform/services-platform/example-service.git
//     cd example-service
//     git checkout binary-build-deployment
//     '''
// }

def pushChart(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://helm-charts/${args.namespace}/beta/"
}


// go mod download
// go mod verify
// go mod tidy -v 