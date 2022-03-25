def call() {
  def config = pipelineCfg()
  def envar = checkoutCode()
  print("ini :" + envar)
  if(envar.environment == 'development' || envar.environment  == 'staging'){
                    container('docker') {
                        echo "Running Docker Build"
                        dockerBuild(registry_url: config.url_images_registry, image_name: config.service_name, image_version: envar.version)
                    }
          }
  if(envar.environment  == 'production'){
                    container('docker') {
                        echo "Running Docker Build"
                        dockerBuild2(registry_url: config.url_images_registry, image_name: config.service_name, dstVersion: "${config.Tag}-${BUILD_NUMBER}")
                    }
          }else{
            skip()
          }        
}

def dockerBuild(Map args) {
  sh "docker build -t ${args.registry_url}/${args.image_name}:${args.image_version} ."
  sh 'docker images'
}      

def dockerBuild2(Map args) {
  sh "docker build -t ${args.registry_url}/${args.image_name}:${args.dstVersion} ."
  sh 'docker images'
}

def skip(){
    sh "echo '=============== SKIP =============='"
}
