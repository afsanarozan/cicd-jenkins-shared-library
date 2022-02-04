def call(Map var) {
//   def envar = checkoutTagging()
//   def setting = settings()
    echo "Deploy to kubernetes "
    container('base'){
            dir('helm') {
                withKubeConfig([credentialsId: 'nonprod-cluster']) {
                script {
                sh """
                    kubectl config get-contexts
                    cd ${var.service_name}
                """
                   
                }
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
    sh "helm upgrade ${args.service_name} . -f values.yaml -n ${args.name_space}"
}

def helmInstall(Map args) {
    sh "helm install ${args.service_name} . -f values.yaml -n ${args.name_space}"
}