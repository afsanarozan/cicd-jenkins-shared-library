def call() {
    def directories = sh(
            returnStdout: true,
            script: 'find * -maxdepth 0 -type d')
            .trim().split('\r?\n')

    container('docker') {
        withCredentials(
            [usernamePassword(
                credentialsId: 'artifactory-finterlabs',
                usernameVariable: 'USERNAME',
                passwordVariable: 'PASSWORD')]) {
                sh 'docker login $DOCKER_FLABS -u $USERNAME -p $PASSWORD'
                }

            for (f in directories) {
            dir(f) {
                    sh 'pwd'
                    String name = "${env.DOCKER_FLABS}/${env.GROUP_IMAGE}/${env.GROUP_NAME}-${f}:${env.IMAGE_TAG}"
                    dockerBuilder(name)
            }
            }
    }
}

def dockerBuilder(String name) {
    sh "docker build -t ${name} ."
    sh "docker push ${name}"
}
