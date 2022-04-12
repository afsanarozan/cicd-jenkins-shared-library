def call() {
  
  sh "printenv | sort"
  echo "Let's Deploy Platform"
    container('base') {
        withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
            installCli()
            dir("script") {
            sh "./deploy-platform.sh"
            }
        }
    }
    // container('base') {
    //     withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
    //         sh "kubectl get ns"
    //     }
    // }
}

def installCli(){
    sh """
        whoami
        yum upgrade
        yum update 
        yum install curl -y 
        yum install wget -y
        yum install tar -y
        yum install snapd -y

        wget https://get.helm.sh/helm-v3.8.0-linux-amd64.tar.gz
        tar -zxvf helm-v3.8.0-linux-amd64.tar.gz
        rm helm-v3.8.0-linux-amd64.tar.gz
        mv linux-amd64/helm /usr/local/bin/helm
        helm version
        
        wget -qO /usr/local/bin/yq https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64
        chmod a+x /usr/local/bin/yq
        yq --version
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

