def call(Map envar) {
    def config = pipelineCfg()
    echo "Running Helm Package"
    dir('Charts') {
        container('helm'){
            // checkout([$class: 'GitSCM', branches: [[name: "master"]], userRemoteConfigs: [[credentialsId: "6c8b6848-ca10-4862-b1a9-fe3e6d46da61", url: "${envar.helm_git}"]]])
            helmLint(service_name: config.service_name)
            helmPackage(service_name: config.service_name)
        }
    }
}

def helmLint(Map args) {
    echo "Running helm lint"
    sh "ls"
    sh "pwd"
    sh "helm lint ${args.service_name}"
}

def helmPackage(Map args) {
    echo "Running Helm Package"
    sh "helm package ${args.service_name}"
}