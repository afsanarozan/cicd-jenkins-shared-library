def call(Map var) {
//   def envar = checkoutTagging()
//   def setting = settings()
    echo "Deploy to kubernetes "
    container('base'){
            dir('Charts') {
                    withKubeConfig([credentialsId: 'nonprod-cluster']) {
                    try {
                        helmUpgrade(service_name: var.service_name, name_space: var.name_space)
                    } catch (e) {
                        helmInstall(service_name: var.service_name, name_space: var.name_space)
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
    sh "helm install ${args.service_name} . -f values.yaml -n ${args.name_space}"
}