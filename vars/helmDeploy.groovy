def call(Map var) {
//   def envar = checkoutTagging()
//   def setting = settings()
    echo "Deploy to kubernetes "
    container('base'){
            dir('helm') {
                withKubeConfig([credentialsId: 'nonprod-cluster']) {
                // script {
                // sh """
                //     kubectl config get-contexts
                //     kubectl config use-context do-sgp1-labs-nonproduction  
                //     cd ${args.service_name}
                //     helm upgrade ${args.service_name} . -f values.yaml -n ${args.name_space}
                // """
                   
                // }
                    try {
                        helmUpgrade(service_name: var.service_name, service_name: var.service_name)
                    } catch (e) {
                        helmInstall(service_name: var.service_name, service_name: var.service_name)
                    }
            }
        }
    }       
}

def helmUpgrade(Map args) {
    sh "helm upgrade ${args.service_name} . -f values.yaml -n ${args.name_space}"
}

def helmUpgrade(Map args) {
    sh "helm kafka ${args.service_name} . -f values.yaml -n ${args.name_space}"
}