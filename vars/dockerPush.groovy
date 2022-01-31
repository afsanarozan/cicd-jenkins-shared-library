def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  def setting = settings()

                    container('docker') {
                        echo "Running Docker Push"
                        echo "${setting.url_images_registry}"
                        docker.withRegistry("${setting.url_images_registry}",credentials: token_registry) {
                        dockerPush(registry_url: setting.url_images_registry, image_name: config.service_name, token: setting.token_registry)
                    }
                }        
}

def dockerPush(Map args) {
    sh 'docker push ${args.registry_url}/${args.image_name}:beta'
    sh 'docker rmi ${args.registry_url}/${args.image_name}:beta'
}
                        
