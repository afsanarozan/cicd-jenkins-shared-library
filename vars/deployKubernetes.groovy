def call(Map var) {
    def config = pipelineCfg()
    echo "Deploy to kubernetes "
    container('base'){
            withKubeConfig([credentialsId: 'nonprod-cluster']) {
            try {
                kubectlApply(service_name: config.service_name, name_space: config.name_space)
            } catch (e) {
                helmInstall(service_name: config.service_name, name_space: config.name_space)
            }
        }
    }       
}

def kubectlApply(Map args) {
    sh """
    kubectl apply -f Deployment/ -n testing
    """
}

def helmInstall(Map args) {
    sh "cd ${args.service_name}"
    sh "helm install ${args.service_name} . -f values.yaml -n ${args.name_space}"
}