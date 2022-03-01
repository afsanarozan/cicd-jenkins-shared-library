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
    kubectl apply -f Deployment/ -n ${args.name_space}
    """
}

def kubectlInstall(Map args) {
    sh "kubectl install -f Deployment/ -n ${args.name_space}"
}