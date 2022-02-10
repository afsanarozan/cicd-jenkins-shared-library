def call(Map envar) {
    container('docker') {
        echo "Running Docker Push"
        echo "${envar.container_registry}"
        sh 'docker login registry.digitalocean.com -u d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b -p d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b'
        dockerPush(registry_url: envar.container_registry, image_name: envar.image_name)
    }        
}

def dockerPush(Map args) {
    sh "docker push ${args.registry_url}/${args.image_name}:beta"
    sh "docker rmi ${args.registry_url}/${args.image_name}:beta"
}
                        
