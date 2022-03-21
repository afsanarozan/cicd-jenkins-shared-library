def call {
    sshPublisher(
        publishers: [
            sshPublisherDesc(
                configName: 'vm-do-testing',
                verbose: false,
                transfers: [
                    sshTransfer(
                        remoteDirectory: "/home",
                        execCommand: "whoami",
                        execTimeout: 60000,
                    )
                ]
            )
        ]
    )
}