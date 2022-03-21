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
                            execCommand: "go version",
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