def call(Map args) {
//   def envar = checkoutTagging()
//   def setting = settings()
    echo "Deploy to kubernetes "
    container('base'){
            dir('helm') {
                withKubeConfig([credentialsId: 'nonprod-cluster']) {
                script {
                sh """
                    kubectl config get-contexts
                    kubectl config use-context do-sgp1-labs-nonproduction  
                    cd ${args.service_name}
                    helm upgrade ${args.service_name} . -f values.yaml -n ${args.name_space}
                """
                }
            }
        }
    }       
}
