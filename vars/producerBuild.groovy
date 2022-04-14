def call(String service) {
    dir(service) {
        sh 'pwd'
        String name = "${env.DOCKER_FLABS}/${env.GROUP_IMAGE}/${env.GROUP_NAME}-${service}:${env.IMAGE_TAG}"
        dockerBuilder(name)
    }
}

def dockerBuilder(String name) {
    container('docker') {
        sh "docker build -t ${name} ."
        sh "docker push ${name} -u admin -p @klik123"
    }
}
