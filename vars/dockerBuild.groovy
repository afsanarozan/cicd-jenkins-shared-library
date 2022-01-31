def call(Map envar) {
    def setting = settings()

    if(envar.environment == 'sit' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('docker'){
            echo "Running Docker Build"
            dockerBuild(container_registry: setting.container_registry, image_name: envar.application_name, version: envar.version, build_number: envar.build_number)
            dockerSave(container_registry: setting.container_registry, image_name: envar.application_name, version: envar.version, build_number: envar.build_number)
        }

      //  publishBuildInfo(application_name: envar.application_name, build_number: envar.build_number)
    }

} 

def dockerBuild(Map args) {
    sh "docker build -t ${args.container_registry}/${args.image_name}:${args.version}-${args.build_number} ."
}

def dockerSave(Map args) {
    sh "docker save ${args.container_registry}/${args.image_name}:${args.version}-${args.build_number} > ${args.image_name}-${args.version}-${args.build_number}.tar "
}
/*
def publishBuildInfo(Map args) {
    container('jfrog-go') {
        echo "publish build info"
        sh "jfrog rt bp ${args.application_name} ${args.build_number}"
    }
}
*/