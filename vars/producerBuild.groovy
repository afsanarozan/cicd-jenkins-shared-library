def call(String service) {
    sh "cd ${service}"
    String name = "${env.DOCKER_FLABS}/${env.GROUP_IMAGE}/${env.GROUP_NAME}-${service}:${env.IMAGE_TAG}"
    DockerBuilder(name)
    sh 'cd ..'
}

def DockerBuilder(String name) {
    container('docker') {
        sh "docker built -t ${name} ."
        sh "docker push ${name}"
    }
}
