def call(Map envar) {
    def setting = settings()
    
    if(envar.environment == 'sit' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('jfrog-go') {
            withCredentials([string(credentialsId: 'access-token', variable: 'TOKEN')]) { 
                //fixing warning
                sh "jfrog c add jfrog-cloud --url ${setting.artifactory_url} --access-token \$TOKEN"
                sh "jfrog rt go-config --server-id-resolve jfrog-cloud --repo-resolve klik-go --server-id-deploy jfrog-cloud --repo-deploy klik-go"
            }
        sh "jfrog config show"
        sh "jfrog rt go build -o ${envar.version}-${envar.application_name} --build-name=${envar.application_name} --build-number=${envar.build_number}"
        echo "publish build package"
        sh "jfrog rt gp v1.1.${envar.build_number} --build-name ${envar.application_name} --build-number ${envar.build_number}"
        echo "publish build info"
        sh "jfrog rt bp ${envar.application_name} ${envar.build_number}"

        }
    }
}

    