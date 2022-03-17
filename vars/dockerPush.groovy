def call() {
   def config = pipelineCfg()
   def envar = checkoutCode()
    if(envar.environment == 'dev' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('docker') {
            echo "Running Docker Push"
            sh 'docker login ${setting.do_url} -u ${setting.do_token_registry} -p ${setting.do_token_registry}'
            dockerPush(registry_url: config.url_images_registry, image_name: config.service_name, srcVersion: envar.version, dstVersion: "${envar.version}-${BUILD_NUMBER}")
        }
    }else{
    skip()
}
}
def dockerPushTag(Map args) {
    sh "docker tag ${args.registry_url}/${args.image_name}:${args.srcVersion} ${args.registry_url}/${args.image_name}:${args.dstVersion}"
    sh "docker push ${args.registry_url}/${args.image_name}:${args.dstVersion}"
    sh "docker rmi ${args.registry_url}/${args.image_name}:${args.dstVersion}"
}

def skip(){
    sh "echo '=============== SKIP =============='"
}
