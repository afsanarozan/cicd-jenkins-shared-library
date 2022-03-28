def call(Map args){
    def config = pipelineCfg()
    sh "echo binary build"
    container ('golang'){
        sh 'ls'
        sh """
            go version
            git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kds-platform/plugin.git".insteadOf "https://gitlab.com/kds-platform/plugin.git"
            go mod download
            go mod verify
            go mod tidy -v 
            go build -o ${config.service_name}  
            ls -la
        """
    }

    container('aws-cli'){
        sh "aws s3 cp ${config.service_name} --endpoint-url ${config.spaces_url} s3://binary-build/beta"
    }
}


def pushChart(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://helm-charts/${args.namespace}/beta/"
    
}


// go mod download
// go mod verify
// go mod tidy -v 