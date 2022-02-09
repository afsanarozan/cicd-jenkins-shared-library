def call(Map envar) {
        container('docker'){
            echo "Running Docker Build"
            dockerBuild(container_registry: envar.container_registry, image_name: envar.image_name, version: envar.tag)
        }
} 

def dockerBuild(Map args) {
    sh "docker build -t ${args.container_registry}/${args.image_name}:${args.version} ."
}

