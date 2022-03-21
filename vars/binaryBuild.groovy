def call {
    // script {
    //     sshPublisher(
    //         publishers: [
    //             sshPublisherDesc(
    //                 configName: 'vm-do-testing',
    //                 verbose: false,
    //                 transfers: [
    //                     sshTransfer(
    //                         remoteDirectory: "/home",
    //                         execCommand: "whoami",
    //                         execTimeout: 60000
    //                     )
    //                 ]
    //             )
    //         ]
    //     )
    // }
    def remote = [:]
        remote.name = 'vm-do-testing'
        remote.host = '178.128.97.157'
        remote.user = 'root'
        remote.password = '2021klikLabs'
        remote.allowAnyHosts = true
    
    sshDeploy(remote)
}

def sshDeploy(Map args){
    sshCommand remote: remote, command: "ls -lrt"
}