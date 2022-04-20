def call(Map args) {
    def config = pipelineCfg()
    def envar = checkoutCode()
    echo "Running Helm Push"

    if (envar.environment  == 'production') {
        envar.environment = 'release'
    } else if (envar.environment  == 'staging') {
        envar.environment = 'beta' 
    } else {
        envar.environment = 'alpha'
    }
    
    dir('Charts') {
        sh "ls"
            container('aws-cli'){
            pushChart(service_name: config.service_name, spaces_url: config.spaces_url, application_name: config.application_name, environment: envar.environment, dstVersion: "${BUILD_NUMBER}")
        }
    }
}

def pushChart(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://helm-charts/${args.application_name}/${args.environment}/"
    // sh "aws s3 cp s3://helm-charts/${args.namespace}/beta/${args.service_name}-*.tgz s3://helm-charts/${args.namespace}/beta/${args.service_name}-${args.dstVersion}.tgz"
}
