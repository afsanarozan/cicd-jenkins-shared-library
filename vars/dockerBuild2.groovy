def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  def setting = settings()

                    container('Docker') {
                        sh 'docker images'
                        docker.build("setting.url_images_registry/config.service_name:beta")
                    }
}

