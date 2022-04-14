def call() {
    def files = findFiles()

    container('docker') {
        withCredentials(
            [usernamePassword(
                credentialsId: 'artifactory-finterlabs',
                usernameVariable: 'USERNAME',
                passwordVariable: 'PASSWORD')]) {
                sh 'docker login $DOCKER_FLABS -u $USERNAME -p $PASSWORD'
                }

        files.each {f ->
            if (f.directory && !f.directory.is('.git')) {
                dir(f.name) {
                    sh 'pwd'
                    String name = "${env.DOCKER_FLABS}/${env.GROUP_IMAGE}/${env.GROUP_NAME}-${f.name}:${env.IMAGE_TAG}"
                    dockerBuilder(name)
                }
            }
        }
    }
}

def dockerBuilder(String name) {
    sh "docker build -t ${name} ."
    sh "docker push ${name}"
}
