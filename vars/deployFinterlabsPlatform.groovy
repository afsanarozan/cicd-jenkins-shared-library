def call() {
  
  sh "printenv | sort"
  echo "Let's Deploy Platform"

    container('base'){
        withKubeConfig([credentialsId: DOcredential]) {
            helmFinterlabs(service_name: config.service_name, name_space: namespace)
        }
    }       

}

def helmFinterlabs(Map args) {
    sh """
    ls
    kubectl get ns
    helm upgrade ${args.service_name} --install Charts/${args.service_name} -f ${values} -n ${args.name_space} --set image.tag=${env}-${BUILD_NUMBER}
    """
}

