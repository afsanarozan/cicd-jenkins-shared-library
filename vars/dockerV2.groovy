def call() {
   def config = pipelineCfg()
   def envar = checkoutCode()
   def args = [:]
   args.registry_url = config.url_images_registry
   args.image_name = config.service_name

    container('docker') {
        sh 'docker login registry.digitalocean.com -u d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b -p d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b'
        if (envar.branch == '*/development') {

        } else if (envar.environment == 'staging') {

        } else if(envar.environment == 'production') {

        }else {
            skip()
        }
    }

}

def tagImage(Map args, String src , String, dst) {
    echo "Running Docker tag"
      sh "docker tag ${args.registry_url}/${args.image_name}:${src} ${args.registry_url}/${args.image_name}:${dst}"
}
def pushImage(Map args, String version) {
    echo "Running Docker Push"
    sh "docker push ${args.registry_url}/${args.image_name}:${version}"
}
def removeImage(Map args, String version) {
    echo "Running Docker remove image"
    sh "docker rmi ${args.registry_url}/${args.image_name}:${version}"
}

def skip(){
    sh "echo '=============== SKIP =============='"
}
