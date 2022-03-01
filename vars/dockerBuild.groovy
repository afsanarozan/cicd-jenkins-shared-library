def call(Map envar) {
      def config = pipelineCfg()
        container('docker'){
            echo "Running Docker Build"
            dockerBuild(container_registry: config.container_registry, image_name: config.image_name, version: config.tag)
        }
} 

def dockerBuild(Map args) {
    sh "cat config.yaml"
    sh "docker build -t ${args.container_registry}/${args.image_name}:${args.version} ."
}

