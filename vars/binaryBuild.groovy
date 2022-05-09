def call(Map args){
    def config = pipelineCfg()
    def envar = checkoutCode()
    sh "echo binary build"

    if (envar.environment  == 'production') {
        envar.environment = 'release'
    } else if (envar.environment  == 'staging') {
        envar.environment = 'beta' 
    } else {
        envar.environment = 'alpha'
    }

    echo "${envar.environment}"

    if(envar.environment == 'alpha' || envar.environment  == 'beta'){
        container ('golang'){
        sh 'ls'
            buildBinary(service_name: config.service_name, version: envar.version, )
        }
        container('aws-cli'){
            pushBinary(service_name: config.service_name, spaces_url: config.spaces_url, environment: envar.environment, application_name: config.application_name, dstVersion: "${BUILD_NUMBER}")
        }
    }
}

def buildBinary(Map args) {
    sh """
        go version
        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kds-platform/plugin.git".insteadOf "https://gitlab.com/kds-platform/plugin.git"
        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kds-platform/library".insteadOf "https://gitlab.com/kds-platform/library"
        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kds-platform/apim/notification-service".insteadOf "https://gitlab.com/kds-platform/apim/notification-service.git"
        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kliklab/jose-ported.git".insteadOf "https://gitlab.com/kliklab/jose-ported.git"
        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kliklab/libs.git".insteadOf "https://gitlab.com/kliklab/libs.git"
        go mod verify
        go mod tidy -e 
        go build -o ${args.service_name}  
        ls -la
    """
}

def pushBinary(Map args) {
    echo "Push Chart"
    //sh "aws s3 --recursive mv s3://binary-build/${args.application_name}/${args.environment}/${args.service_name} --endpoint-url ${args.spaces_url} s3://binary-build/${args.application_name}/${args.environment}/${args.service_name}-${args.dstVersion}"
    try {
        sh "aws s3 ${args.service_name} --endpoint-url ${args.spaces_url} s3://binary-build/${args.application_name}/${args.environment}/${args.service_name}-latest ./${args.service_name}-${args.dstVersion}"
        sh "aws s3 ${args.service_name} --endpoint-url ${args.spaces_url} ./${args.service_name}-${args.dstVersion} s3://binary-build/${args.application_name}/${args.environment}/"
        sh "rm ${args.service_name}-${args.dstVersion}"

    } catch (e) {
        echo "first time push binary build service to do spaces"
    } finally {
        sh "aws s3 cp ${args.service_name} --endpoint-url ${args.spaces_url} s3://binary-build/${args.application_name}/${args.environment}/"
    }
}