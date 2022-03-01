def call(Map envar) {
def config = pipelineCfg()
        container('sonarscanner'){
        echo "Running Code Review With SonarQube"
                withSonarQubeEnv(credentialsId: "${config.credential_sonarqube}", installationName: 'sonarqube') {
                sonarScanGo(name_space: config.name_space, service_name: config.service_name, sonarSrc: config.sonarSrc)
                }       
        }
}

def sonarScanGo(Map args){
        sh " sonar-scanner -X \
        -Dsonar.projectKey=${args.name_space}-${args.service_name} \
        -Dsonar.sources=${args.sonarSrc} \
        -Dsonar.qualitygate.wait=true \
        -Dsonar.language=go \
        -Dsonar.tests=. \
        -Dsonar.go.coverage.reportPaths=coverage.out"
}
