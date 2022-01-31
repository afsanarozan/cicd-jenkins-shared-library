def call(Map envar) {
    def setting = settings()
    echo "language ${envar.language}"

    switch(envar.language) {
        case "golang":
            echo "golang Dependency download"
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
            break;
        case "node":
            echo "trigger npm audit"
                    container('jfrog-go') {
                        withCredentials([string(credentialsId: 'access-token', variable: 'TOKEN')]) { 
                            //fixing warning
                            sh "jfrog c add jfrog-cloud --url ${setting.artifactory_url} --access-token \$TOKEN"
                            sh "jfrog rt npmc --server-id-resolve jfrog-cloud --repo-resolve klik-npm --server-id-deploy jfrog-cloud --repo-deploy klik-npm"
                        }
                        sh "jfrog xr an"
                    }
            break;
        case "maven":
            echo "trigger maven audit"
                    container('jfrog-go') {
                        withCredentials([string(credentialsId: 'access-token', variable: 'TOKEN')]) { 
                            //fixing warning
                            sh "jfrog c add jfrog-cloud --url ${setting.artifactory_url} --access-token \$TOKEN"
                            sh "jfrog rt mvn-config --server-id-resolve jfrog-cloud --repo-deploy-releases klik-maven --repo-deploy-snapshots klik-maven --repo-resolve-releases klik-maven --repo-resolve-snapshots klik-maven --server-id-deploy jfrog-cloud --server-id-resolve jfrog-cloud"
                        }
                        sh "jfrog xr am"
                    }
            break;
    }
}