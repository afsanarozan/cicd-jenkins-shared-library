def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  def setting = settings()

                    container('docker') {
                        echo "Running Docker Push"
                        sh 'docker login -u setting.token_registry -p setting.token_registry https://registry.digitalocean.com'
                        dockerPush ( registry_url: setting.url_images_registry, image_name: config.service_name)
                    }
}

def dockerPush(Map args) {
    sh 'docker push ${args.registry_url}/${args.image_name}:beta'
    sh 'docker rmi ${args.registry_url}/${args.image_name}:beta'
}
                        
