def call(Map envar) {
    def setting = settings()
    
    if(envar.environment == 'sit' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('jfrog-go') {
       
       /*
        withCredentials([string(credentialsId: 'access-token', variable: 'TOKEN')]) { 
            //fixing warning
            sh "jfrog c add jfrog-cloud --url ${setting.artifactory_url} --access-token \$TOKEN"
            sh "jfrog rt npmc --server-id-resolve jfrog-cloud --repo-resolve klik-npm --server-id-deploy jfrog-cloud --repo-deploy klik-npm"
        }
        */
        
    
        sh "jfrog config show"
        sh "jfrog rt npm-install --build-name=${envar.application_name} --build-number=${envar.build_number}"
        //generate information
        sh "jfrog rt bag ${envar.application_name} ${envar.build_number}"
        //check environment variable
        sh "jfrog rt bce ${envar.application_name} ${envar.build_number}"
        
        }
       // sh "tar zcvf ${envar.version}-${envar.application_name}.tar ./build --warning=no-file-changed"
    }
}

    