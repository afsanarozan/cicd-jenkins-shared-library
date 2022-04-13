def call() {
    sh "printenv | sort"
    echo "Let's Deploy Platform"
    container('ubuntu') {
        withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
            if (env.platform == "----------all platform enabled----------") {
                installCli()
                dir("script") {
                sh "./deploy-platform.sh"
                }
            } else {
                echo "let's install ${env.platform}"
                deployPlatform()
            }   
        } 
    }
}

def installCli(){
    sh """
        apt-get upgrade && apt-get update 
        apt-get install curl -y 
        apt-get install wget -y
        apt-get install tar -y

        # helm
        wget https://get.helm.sh/helm-v3.8.0-linux-amd64.tar.gz
        tar -zxvf helm-v3.8.0-linux-amd64.tar.gz
        rm helm-v3.8.0-linux-amd64.tar.gz
        mv linux-amd64/helm /usr/local/bin/helm
        helm version
        
        # cli yq
        wget -qO /usr/local/bin/yq https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64
        chmod a+x /usr/local/bin/yq
        yq --version

        # kubectl 
        apt-get install -y apt-transport-https ca-certificates curl
        curl -fsSLo /usr/share/keyrings/kubernetes-archive-keyring.gpg https://packages.cloud.google.com/apt/doc/apt-key.gpg
        echo "deb [signed-by=/usr/share/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | tee /etc/apt/sources.list.d/kubernetes.list
        apt-get update
        apt-get install -y kubectl
    """
}

def deployPlatform(Map args) {
    sh """
    ls
    helm repo add helm-finterlabs https://artifactory.finterlabs.com/repository/finterlabs-helm-local/ --username ${HELM_USER} --password ${HELM_PASSWORD}
    helm repo update
    helm upgrade --install ${env.platform} helm-finterlabs/${env.platform} -n finterlabs-platform
    """
}


