def call(Map envar) {
    if(envar.environment == 'sit' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('jfrog-go') {
        //sh "jfrog rt bs ${envar.application_name} ${envar.build_number}"    
        sh "jfrog config show"
        sh "jfrog xr s \".\" --watches ${envar.application_name}"
        sh "ls -lrth"
        sh "jfrog xr s ${envar.application_name}-${envar.version}-${envar.build_number}.tar --watches ${envar.application_name}"
        }
    }
}

    