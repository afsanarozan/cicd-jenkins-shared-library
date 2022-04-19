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
        git config --global url.https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kds-platform/plugin.git.insteadOf https://gitlab.com/kds-platform/plugin.git
        go mod verify
        go mod tidy -v 
        go build -o ${args.service_name}  
        ls -la
    """
}

def pushBinary(Map args) {
    echo "Push Chart"
    //sh "aws s3 --recursive mv s3://binary-build/${args.application_name}/${args.environment}/${args.service_name} --endpoint-url ${args.spaces_url} s3://binary-build/${args.application_name}/${args.environment}/${args.service_name}-${args.dstVersion}"
    sh "aws s3 cp ${args.service_name} --endpoint-url ${args.spaces_url} s3://binary-build/${args.application_name}/${args.environment}/${args.service_name}"
}