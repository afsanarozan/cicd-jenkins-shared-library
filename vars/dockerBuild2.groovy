def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()

                    container('docker') {
                        echo "Running Docker Build"
                        dockerBuild(registry_url: config.url_images_registry, image_name: config.service_name)
                    }
}

def dockerBuild(Map args) {
  sh "docker build -t ${args.registry_url}/${args.image_name}:beta ."
  sh 'docker images'
}      


