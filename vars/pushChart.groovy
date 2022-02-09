def call(Map envar) {
    echo "Running Helm Push"
    
    dir('Charts') {
        sh "ls"
            container('aws-cli'){
            pushChart(service_name: envar.service_name, spaces_url: envar.spaces_url)
        }
    }
}

def pushChart(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://helm-charts/${args.service_name}-*.tgz"
}
