def call(Map envar) {
    def setting = settings()
    
    if(envar.environment == 'sit' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('jfrog-go') {
        sh "jfrog config show"
        sh "jfrog rt mvn clean install  -f pom.xml --build-name=${envar.application_name} --build-number=${envar.build_number}"
        //generate information
        }
    }
}

    