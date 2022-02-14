def call() {
   def config = pipelineCfg()
   def envar = checkoutCode()
    if(envar.environment == 'dev' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('docker') {
            echo "Running Docker Push"
            sh 'docker login registry.digitalocean.com -u d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b -p d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b'
            dockerPush(registry_url: config.url_images_registry, image_name: config.service_name, srcVersion: ${envar.version}, dstVersion: "${envar.version}-${BUILD_NUMBER}")
        }
    }else{
    skip()
}
}
def dockerPush(Map args) {
    sh "echo ${args.registry_url}"
    sh "docker push ${args.registry_url}/${args.image_name}::${args.srcVersion}"
    sh "docker rmi ${args.registry_url}/${args.image_name}::${args.srcVersion}"
}

def skip(){
    sh "echo '=============== SKIP =============='"
}
