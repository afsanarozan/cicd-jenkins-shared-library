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
                            checkout([$class: 'GitSCM', branches: [[name: "binary-build-deployment"]], userRemoteConfigs: [[credentialsId: "${config.credential}", url: "${config.repo_url}"]]]),
                            execCommand: '''
                            ls
                            // export PATH=$PATH:/usr/local/go/bin;
                            // git clone https://automation:glpat-Vf6rMnhFzEbshHrj2TYQ@gitlab.com/kliklab/automation-platform/services-platform/example-service.git;
                            // cd example-service;
                            // git checkout binary-build-deployment;
                            // go version; 
                            // go build .
                            ''',
                            execTimeout: 60000
                        )
                    ]
                )
            ]
        )
    }
    sh "echo binary build"
    //     def remote = [:]
    //         remote.name = 'vm-do-testing'
    //         remote.host = '178.128.97.157'
    //         remote.user = 'root'
    //         remote.password = '2021klikLabs'
    //         remote.allowAnyHosts = true
        
    //     sshDeploy(remote)
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

// go mod download
// go mod verify
// go mod tidy -v 