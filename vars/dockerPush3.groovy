def call(Map envar) {
    def config = pipelineCfg()
    container('docker') {
        echo "Running Docker Push"
        echo "${config.container_registry}"
        sh 'docker login registry.digitalocean.com -u d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b -p d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b'
        dockerPush(registry_url: config.container_registry, image_name: config.image_name)
    }        
}

def dockerPush(Map args) {
    sh "docker push ${args.registry_url}/${args.image_name}:beta"
    sh "docker rmi ${args.registry_url}/${args.image_name}:beta"
}
                        
