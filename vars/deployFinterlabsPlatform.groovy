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
        whoami
        apt-get upgrade
        apt-get update 
        apt-get install snapd
        snap install helm --classic
        snap install yq
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

