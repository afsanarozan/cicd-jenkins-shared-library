def call(Map envar) {
    echo "Running Helm Package"
    echo "${envar.helm_git}"
    dir('helm') {
        checkout([$class: 'GitSCM', branches: [[name: "master"]], userRemoteConfigs: [[credentialsId: "6c8b6848-ca10-4862-b1a9-fe3e6d46da61", url: "${envar.helm_git}"]]])
        sh "ls"
        helmLint(service_name)
        helmPackage(service_name)
    }
    sh "ls"
}

def helmLint(String service_name) {
    echo "Running helm lint"
    sh "helm lint ${service_name}"
}

def helmPackage(String service_name) {
    echo "Running Helm Package"
    sh "helm package ${service_name}"
}