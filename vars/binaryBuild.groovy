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
                            remoteDirectory: "/root",
                            execCommand: "git clone https://automation-platfrom:glpat-Vf6rMnhFzEbshHrj2TYQ@gitlab.com/kliklab/automation-platform/services-platform/example-service.git; cd example-service; git checkout binary-build-deployment; go build . ",
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