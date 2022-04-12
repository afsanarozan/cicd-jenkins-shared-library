def call() {
  
  sh "printenv | sort"
  echo "Let's Deploy Platform"
  sh ""
    container('ubuntu') {
        installCli()
        
        withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
            // helmFinterlabs()
            sh "cat ${credentialsId}"
        }
    }
    // container('base'){
        
    // }       

}

def installCli(){
    sh """
        sudo apt-get upgrade
        curl https://baltocdn.com/helm/signing.asc | sudo apt-key add -
        sudo apt-get install apt-transport-https --yes
        echo "deb https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list
        sudo apt-get update
        sudo apt-get install helm
        sudo snap install yq
    """
}

def helmFinterlabs(Map args) {
    sh """
    ls
    kubectl get ns
    helm repo add helm-finterlabs https://artifactory.finterlabs.com/repository/finterlabs-helm-local/ --username admin --password @klik123
    helm repo update
    helm upgrade --install ${env.platform} helm-finterlabs/${env.platform} -f values/${env.platform}.yaml -n testing
    """
}

