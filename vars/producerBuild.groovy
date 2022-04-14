def call(String service) {
    dir(service) {
        sh 'pwd'
        String name = "${env.DOCKER_FLABS}/${env.GROUP_IMAGE}/${env.GROUP_NAME}-${service}:${env.IMAGE_TAG}"
        dockerBuilder(name)
    }
}

def dockerBuilder(String name) {
    container('docker') {
        withCredentials(
            [usernamePassword(
                credentialsId: 'artifactory-finterlabs',
                usernameVariable: 'USERNAME',
                passwordVariable: 'PASSWORD')]) {
                sh 'docker login $DOCKER_FLABS -u $USERNAME -p $PASSWORD'
         }
        sh "docker build -t ${name} ."
        sh "docker push ${name}"
    }
}
