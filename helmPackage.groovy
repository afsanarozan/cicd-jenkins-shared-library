def call(Map envar) {
    echo "Running Helm Package"
    echo "${envar.url}"
    dir('helm') {
        checkout([$class: 'GitSCM', branches: [[name: "master"]], userRemoteConfigs: [[credentialsId: envar.credential, helm_git: envar.helm_git]]])
        sh "ls"
    }
}