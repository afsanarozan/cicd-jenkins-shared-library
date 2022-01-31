def call(Map envar) {
   
    if(envar.environment == 'sit' || envar.environment  == 'staging' || envar.environment  == 'production'){
        container('golang') {
            withCredentials([usernamePassword(credentialsId: 'jfrog-credential ', usernameVariable: 'USER', passwordVariable: 'PASSWORD')]) { 
                echo "running golang dependency download..."
                sh "go env -w GOSUMDB=off"
                sh "go env -w CGO_ENABLED=0"
                sh "go env -w GOPROXY=https://\$USER:\$PASSWORD@klikdevsecops.jfrog.io/artifactory/api/go/klik-go" //fix warning
                sh "go mod download"
                sh "go mod vendor"
            }
        }
    }
}

    