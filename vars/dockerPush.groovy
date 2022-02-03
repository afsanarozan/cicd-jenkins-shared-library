def call(Map envar) {
//   def config = pipelineCfg()
//   def envar = checkoutTagging()
//  def setting = settings()

                    container('docker') {
                        echo "Running Docker Push"
                        echo "${envar.container_registry}"
                        docker.withRegistry("https://${envar.container_registry}", token_registry) {
                        echo "${setting.container_registry}"
                        dockerPush(registry_url: envar.container_registry, image_name: envar.image_name, token: envar.token_registry)
                    }
                }        
}

def dockerPush(Map args) {
    sh 'docker push ${args.registry_url}/${args.image_name}:beta'
    sh 'docker rmi ${args.registry_url}/${args.image_name}:beta'
}
                        
