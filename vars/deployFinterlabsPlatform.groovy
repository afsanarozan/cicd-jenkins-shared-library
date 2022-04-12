def call() {
  
  sh "printenv | sort"
  echo "Let's Deploy Platform"
    container('ubuntu') {
        withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
            installCli()
            dir("script") {
            sh "./deploy-platform.sh"
            sh "helm "
            }
        }
    }
}

def installCli(){
    sh """
        whoami
        apt-get upgrade
        apt-get update 
        apt-get install curl -y 
        apt-get install wget -y
        apt-get install tar -y
        apt-get install snapd -y

        wget https://get.helm.sh/helm-v3.8.0-linux-amd64.tar.gz
        tar -zxvf helm-v3.8.0-linux-amd64.tar.gz
        rm helm-v3.8.0-linux-amd64.tar.gz
        mv linux-amd64/helm /usr/local/bin/helm
        helm version
        
        wget -qO /usr/local/bin/yq https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64
        chmod a+x /usr/local/bin/yq
        yq --version

        curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
        curl -LO "https://dl.k8s.io/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl.sha256"
        echo "$(cat kubectl.sha256)  kubectl" | sha256sum --check
        install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
        kubectl version --client
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

