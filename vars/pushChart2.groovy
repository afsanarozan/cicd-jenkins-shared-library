def call(Map envar) {
    def config = pipelineCfg()
    def setting = checkoutTagging()
    echo "Running Helm Push"
    
    dir('Charts') {
        sh "ls"
            container('aws-cli'){
            sh "aws s3 cp cms-api-0.1.0.tgz --endpoint-url https://labs-storage.sgp1.digitaloceanspaces.com s3://helm-charts/cms-api-0.1.0.tgz"
        }
    }
}

def helmLint(Map args) {
    echo "Running helm lint"
    sh "helm lint ${args.service_name}"
}

def helmPackage(Map args) {
    echo "Running Helm Package"
    sh "helm package ${args.service_name}"
}
