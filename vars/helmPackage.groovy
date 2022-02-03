def call(Map envar) {
    echo "Running Helm Package"
    echo "${envar.helm_git}"
    dir('helm') {
        checkout([$class: 'GitSCM', branches: [[name: "master"]], userRemoteConfigs: [[credentialsId: "6c8b6848-ca10-4862-b1a9-fe3e6d46da61", url: "${envar.helm_git}"]]])
        sh "ls"
    }
}