def call(String service) {
    sh "cd ${service}"
    String name = "${env.DOCKER_FLABS}/${env.GROUP_IMAGE}/${env.GROUP_NAME}-${service}:${env.IMAGE_TAG}"
    dockerBuilder(name)
    sh 'cd ..'
}

def dockerBuilder(String name) {
    container('docker') {
        sh "docker build -t ${name} ."
        sh "docker push ${name}"
    }
}
