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
    try {
        sh """ 
        aws s3 cp --endpoint-url ${args.spaces_url} s3://helm-charts/${args.application_name}/${args.environment}/${args.service_name}-latest.tgz ./${args.service_name}-${args.dstVersion}.tgz"
        aws s3 cp --endpoint-url ${args.spaces_url} ./${args.service_name}-${args.dstVersion}.tgz s3://helm-charts/${args.application_name}/${args.environment}/
        rm ${args.service_name}-${args.dstVersion}.tgz
        """
    } catch (e) {
        echo "first time push for helm chart service"
    } finally {
        sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://helm-charts/${args.application_name}/${args.environment}/${args.service_name}-latest.tgz"
    }  
    // sh "aws s3 cp s3://helm-charts/${args.namespace}/beta/${args.service_name}-*.tgz s3://helm-charts/${args.namespace}/beta/${args.service_name}-${args.dstVersion}.tgz"
}
