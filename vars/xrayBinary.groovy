def call(Map envar) {
    if(envar.environment == 'sit' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('jfrog-go') {
            switch(envar.language) {
            case "golang":   
                sh "jfrog config show"
                sh "jfrog rt bs ${envar.application_name} ${envar.build_number}"    
                sh "jfrog xr s \".\" --watches ${envar.application_name}"
                break;
            case "node":
                //check configuration
                sh "jfrog config show"  
                //scan all files  
                sh "jfrog xr s \".\" --watches ${envar.application_name}"
                //audit npm build
                sh "jfrog xr an --watches ${envar.application_name}"
                //audit all known vulnerability npm located on current directory 
                sh "jfrog xr an"
                break;
            case "maven":
                sh "jfrog config show"
                //scan file
                sh "jfrog xr s \".\" --watches ${envar.application_name}"
                //scan maven build
                sh "jfrog xr am --watches ${envar.application_name}"    
                //scan all known vulnerability
                sh "jfrog xr am" 
                break;
            }
        }
    }
}


    