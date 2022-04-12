def call() {
  
  sh "printenv | sort"
  echo "Let's Deploy Platform"
    container('ubuntu') {
        withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
            installCli()
            dir("script") {
            deployApp()
            sh "kubectl get ns"
            }
        }
    }
    container('base') {
        withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
            sh "kubectl get ns"
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
    """
}

def deployApp(){
    sh """
    #Execute ENVIRONMENT config
    . ../config/finterlabs-env.sh

    #Deployemnt config file
    CONFIG_FILE=../config/config-deployment.yaml

    #Custom HELM config directory
    NEW_HELM_CONFIG_DIR=../helm-chart

    #Get component that need to be deployed
    COMPONENTS=$(yq eval '.[] | select(.enable == "true").name' $CONFIG_FILE)
    for component in $COMPONENTS; do
    echo --------- Start Deploy $component -------------
    version=$(yq eval '.[] | select(.name == "'$component'").version' $CONFIG_FILE)
    namespace=$(yq eval '.[] | select(.name == "'$component'").namespace' $CONFIG_FILE)
    TGZ_FILE=$component-$version.tgz

    echo Download HELM chart file : $HELM_REPO/$TGZ_FILE
    echo -----------------------------------
    curl -o $TGZ_FILE --user $HELM_USER:$HELM_PASSWORD $HELM_REPO/$TGZ_FILE
    tar xf $TGZ_FILE

    #Check if file custom HELM chart file is exist
    if [[ -f "$NEW_HELM_CONFIG_DIR/$component.yaml" ]]; then
        echo Merge HELM chart default and custom $component
        echo --------------------------------------------------
        yq eval-all "select(fileIndex == 0) *+ select(fileIndex == 1)"  ./$component/values.yaml $NEW_HELM_CONFIG_DIR/$component.yaml >  ./$component/values.yaml.new
        mv ./$component/values.yaml.new ./$component/values.yaml

        #Replace DOMAIN for ingress
        sed -i.bak 's/${DOMAIN}/'${DOMAIN}'/g' ./$component/values.yaml 
    fi

    echo Deploy HELM chart $component
    echo -----------------------------------
    helm --kubeconfig $KUBECONFIG upgrade --timeout 300s --cleanup-on-fail --install $component $component/ -n $namespace --create-namespace
    helm upgrade --install $component helm-finterlabs/$component -f ./$component/values.yaml -n $namespace --create-namespace

    echo Clean downloaded HELM chart $component
    echo -----------------------------------
    rm -rf $TGZ_FILE $component

    echo --------- End Deploy $component -------------
    done;
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

