def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  def setting = settings()

                    container('docker') {
                        echo "Running Docker Push"
                        dockerPush ( registry_url: setting.url_images_registry, image_name: config.service_name, token: setting.token_registry)
                    }
}

def dockerPush(Map args) {
    sh 'docker login -u 0141ac0654a435498fb67862d1b4ba1309a08f190c8bbf780f5b0794b02c09b7 -p 0141ac0654a435498fb67862d1b4ba1309a08f190c8bbf780f5b0794b02c09b7 https://registry.digitalocean.com'
    sh 'docker push ${args.registry_url}/${args.image_name}:beta'
    sh 'docker rmi ${args.registry_url}/${args.image_name}:beta'
}
                        
