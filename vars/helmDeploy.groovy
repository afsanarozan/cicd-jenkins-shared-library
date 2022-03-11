def call(Map var) {
//   def envar = checkoutTagging()
//   def setting = settings()
    def config = pipelineCfg()
    echo "Deploy to kubernetes "
    container('base'){
            dir('Charts') {
                    withKubeConfig([credentialsId: 'nonprod-cluster']) {
                    try {
                        sh "ls"
                        helmUpgrade(service_name: config.service_name, name_space: config.name_space)
                    } catch (e) {
                        sh "ls"
                        helmInstall(service_name: config.service_name, name_space: config.name_space)
                    }
                }
            }
    }       
}

def helmUpgrade(Map args) {
    sh """
    cd ${args.service_name}
    helm upgrade ${args.service_name} . -f values.yaml -n ${args.name_space}
    """
}

def helmInstall(Map args) {
    sh "cd ${args.service_name}"
    sh "ls"
    sh "helm install ${args.service_name} . -f values.yaml -n ${args.name_space}"
}
