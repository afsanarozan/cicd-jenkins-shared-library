def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  def setting = settings()

                    container('docker') {
                        sh 'docker images'
                        docker build -t ${setting.url_images_registry}/${config.service_name}:beta
                    }
}

