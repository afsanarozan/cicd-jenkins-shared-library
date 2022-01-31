def call(Map envar) {
    def setting = settings()
    
    if(envar.environment == 'sit' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('jfrog-go') {
            switch(envar.language) {
            case "golang":   
                //promote build info
                sh "jfrog rt bpr ${envar.application_name} ${envar.build_number} klik-go"
                //upload artifact
                sh "jfrog rt u ${envar.version}-${envar.application_name} klik-go --build-name ${envar.application_name} --build-number ${envar.build_number}"
                sh "jfrog rt u ${envar.application_name}-${envar.version}-${envar.build_number}.tar klik-docker --build-name ${envar.application_name} --build-number ${envar.build_number}"
                dockerPush(
                    container_registry_url: setting.container_registry_url,
                    container_registry: setting.container_registry, 
                    credentialId: setting.registry_credential, 
                    application_name: envar.application_name, 
                    version: envar.version, 
                    build_number: envar.build_number
                )
            break;
            case "node":
                echo "publish build package"
                sh "jfrog rt npm-publish --build-name ${envar.application_name} --build-number ${envar.build_number}"
                echo "publish build info"
                sh "jfrog rt bp ${envar.application_name} ${envar.build_number}"
                break;
            case "maven":
                sh "jfrog rt bp ${envar.application_name} ${envar.build_number}"
                break;
            }
        }
    }
}


def dockerPush(Map args) {
    container('docker') {
        docker.withRegistry(args.container_registry_url, args.credentialId) {
            sh "docker push ${args.container_registry}/${args.application_name}:${args.version}-${args.build_number}"
        }
    }
}