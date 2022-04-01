def call() {
   def config = pipelineCfg()
   def envar = checkoutCode()
    if(envar.environment  == 'production'){
        container('docker') {
            echo "Running Docker Push"
            sh 'docker login registry.digitalocean.com -u d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b -p d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b'
            dockerPush2(registry_url: config.url_images_registry, image_name: config.service_name, srcVersion: envar.version, dstVersion2: "${config.Tag}-${BUILD_NUMBER}")
        }
    }else{
    skip()
}
}

def dockerPush2(Map args) {
    sh "docker pull ${args.registry_url}/${args.image_name}:${args.dstVersion2}"
    sh "docker images"
    //sh "docker tag ${args.registry_url}/${args.image_name}:${args.dstVersion2} ${args.registry_url}/${args.image_name}:${args.dstVersion2}"
    //sh "docker push ${args.registry_url}/${args.image_name}:${args.dstVersion2}"
    //sh "docker rmi ${args.registry_url}/${args.image_name}:${args.dstVersion2}"
}

def skip(){
    sh "echo '=============== SKIP =============='"
}
