def call() {
  
  sh "printenv | sort"
  echo "Let's Deploy Platform"

    container('base'){
        withKubeConfig([credentialsId: "credential_tapera_dev_ali"]) {
            helmFinterlabs()
        }
    }       

}

def helmFinterlabs(Map args) {
    sh """
    ls
    kubectl get ns
    helm repo add helm-finterlabs https://artifactory.finterlabs.com/repository/finterlabs-helm-local/ --username admin --password @klik123
    helm repo update
    helm upgrade --install kafka helm-finterlabs/kafka -f values/kafka.yaml -n testing --set image.tag=${env}-${BUILD_NUMBER}
    """
}

