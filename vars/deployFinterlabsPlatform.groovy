def call() {
    sh "printenv | sort"
    echo "Let's Deploy Platform"
    container('ubuntu') {
        if (env.platform == "----------all platform enabled----------"){
           withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
                installCli()
                dir("script") {
                sh "./deploy-platform.sh"
                }
            } 
        } else {
            echo "let's install ${env.platform}"
            withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
                deployApp(platform: env.platform)
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

def deployApp(Map args) {
    sh """
    ls
    kubectl get ns
    helm repo add helm-finterlabs https://artifactory.finterlabs.com/repository/finterlabs-helm-local/ --username admin --password @klik123
    helm repo update
    helm pull helm-finterlabs/${args.platform}

    tar -zxvf ${args.platform}*.tgz

    if [ -f "../helm-chart/${args.platform}.yaml" ]; then
      echo Merge HELM chart default and custom ${args.platform} : ../helm-chart/${args.platform}.yaml '->' ./${args.platform}/values.yaml
      echo ---------------------------------------------------------------------------------------------------------------
      yq eval-all "select(fileIndex == 0) *+ select(fileIndex == 1)"  ./${args.platform}/values.yaml ../helm-chart/${args.platform}.yaml >  ./${args.platform}/values.yaml.new
      mv ./${args.platform}/values.yaml.new ./${args.platform}/values.yaml

      #Replace DOMAIN for ingress
      sed -i.bak  -e 's/${DOMAIN}/'${DOMAIN}'/g' \
                  -e 's/${PROJECT}/'${PROJECT}'/g' ./${args.platform}/values.yaml 
   fi

    cat ./${args.platform}/values.yaml 
    helm upgrade --install ${args.platform} helm-finterlabs/${args.platform} -f values/${args.platform}.yaml -n finterlabs-platform --create-namespace
    """
}


