def call(Map envar) {
    echo "Running Helm Package"
    echo "${envar.helm_git}"
    container('s3cmd'){
        sh "s3cmd ls"
    }
    // dir('helm') {
    //     checkout([$class: 'GitSCM', branches: [[name: "master"]], userRemoteConfigs: [[credentialsId: "6c8b6848-ca10-4862-b1a9-fe3e6d46da61", url: "${envar.helm_git}"]]])
    //     sh "ls"
    //     helmLint(service_name: envar.service_name)
    //     helmPackage(service_name: envar.service_name)
    // }
    sh "ls"
}

def helmLint(Map args) {
    echo "Running helm lint"
    sh "helm lint ${args.service_name}"
}

def helmPackage(Map args) {
    echo "Running Helm Package"
    sh "helm package ${args.service_name}"
}