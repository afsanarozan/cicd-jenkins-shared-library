def call(Map args) {
    def config = pipelineCfg()
    def envar = checkoutCode()
    def AWS_ACCESS_KEY_ID="YFRP3PS4LIJEOZVRUMMK"
    def AWS_SECRET_ACCESS_KEY="0s4FQ470cF9AGDg7old5fLyvvhbhnqO99ooruvQdVOs"

    echo "Running Helm Push"

    if (envar.environment  == 'production') {
        envar.environment = 'release'
    } else if (envar.environment  == 'staging') {
        envar.environment = 'beta' 
    } else {
        envar.environment = 'alpha'
    }
    dir('Charts') {
        sh "ls -lah"
        container('s3cmd'){
            sh "s3cmd --configure --access_key=${AWS_ACCESS_KEY_ID} --secret_key=${AWS_SECRET_ACCESS_KEY} --region=sgp1 --host=sgp1.digitaloceanspaces.com -s --no-encrypt --dump-config 2>&1 | tee /root/.s3cfg"
            sh "s3cmd get s3://labs-storage/helm-charts/api-gateway/alpha/user-service-0.1.0.tgz" 
            sh "ls -la"
        }
    }
    
    // dir('Charts') {
    //     sh "ls"
    //         container('aws-cli'){
    //         pushChart(service_name: config.service_name, spaces_url: config.spaces_url, application_name: config.application_name, environment: envar.environment, dstVersion: "${BUILD_NUMBER}")
    //     }
    // }
}

def pushChart(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://helm-charts/${args.application_name}/${args.environment}/"
    // sh "aws s3 cp s3://helm-charts/${args.namespace}/beta/${args.service_name}-*.tgz s3://helm-charts/${args.namespace}/beta/${args.service_name}-${args.dstVersion}.tgz"
}
