def call() {
  def config = pipelineCfg()
  def setting = settings()
  def envar = checkoutTagging()
  
  'Container' : {
                stage ("Build Container") {
                    docker.withTool('Docker') {
                        sh 'docker images'
                        docker.build("setting.url_images_registry/config.service_name:beta")
                    }
                }
        }
}
