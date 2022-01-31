def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  def setting = settings()

                    container('docker') {
                        echo "Running Docker Build"
                        dockerBuild(registry_url: setting.url_images_registry, image_name: config.service_name)
                    }
}

def dockerBuild(Map args) {
  sh "docker build -t ${args.registry_url}/${args.image_name}:beta ."
  sh 'docker images'
}      


