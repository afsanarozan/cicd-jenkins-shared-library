def call(Map args){
    def config = pipelineCfg()
    sh "echo binary build"
    container ('golang'){
        sh 'ls'
        sh """
            go version
            go mod verify
            go mod tidy -v 
            go build -o ${config.service_name}  
            ls -la
        """
    }
    container('aws-cli'){
        pushBinary(service_name: config.service_name, spaces_url: config.spaces_url, namespace: config.name_space)
    }
}

def pushBinary(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://binary-build/${args.namespace}/"
}