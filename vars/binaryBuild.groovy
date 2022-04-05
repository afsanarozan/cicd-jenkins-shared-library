def call(Map args){
    def config = pipelineCfg()
    def envar = checkoutCode()
    sh "echo binary build"
    if(envar.environment == 'dev' || envar.environment  == 'staging'){
        container ('golang'){
        sh 'ls'
            buildBinary(service_name: config.service_name, version: envar.version)
        }
        container('aws-cli'){
            pushBinary(service_name: config.service_name, spaces_url: config.spaces_url, environment: envar.environment)
        }
    }
}

def buildBinary(Map args) {
    sh """
        go version
        yum
        go mod verify
        go mod tidy -v 
        go build -o ${args.service_name}  
        ls -la
    """
}

def pushBinary(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name} --endpoint-url ${args.spaces_url} s3://binary-build/${args.environment}/"
}