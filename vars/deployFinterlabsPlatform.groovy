def call() {
  
  sh "printenv | sort"
  echo "Let's Deploy Platform"
  sh ""
    container('ubuntu') {
        installCli()
        withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
            sh "echo testing"
            sh "cat $KUBECONFIG"
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
        apt-get install wget -y
        apt-get install tar -y

        wget https://get.helm.sh/helm-v3.8.0-linux-amd64.tar.gz
        tar -zxvf helm-v3.8.0-linux-amd64.tar.gz
        mv linux-amd64/helm /usr/local/bin/helm
        helm version
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

