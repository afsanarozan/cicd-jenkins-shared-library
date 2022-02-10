def call(Map envar) {
    def config = pipelineCfg()
    echo "Running Helm Push"
    
    dir('Charts') {
        sh "ls"
            container('aws-cli'){
            pushChart(service_name: config.service_name, spaces_url: config.spaces_url)
        }
    }
}

def pushChart(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://helm-charts/ajaruji/beta/"
}
