def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  def setting = settings()

                    container('Docker') {
                        sh 'docker images'
                        sh 'docker login -u setting.token_registry -p setting.token_registry https://registry.digitalocean.com'
                        docker.build("setting.url_images_registry/config.service_name:beta")
                    }
}

