def call(Map envar) {
stageName(nameStage: env.STAGE_NAME)
def config = pipelineCfg()
        container('sonarscanner'){
        echo "Running Code Review With SonarQube"
                withSonarQubeEnv(credentialsId: "sonarqube-token", installationName: 'sonarqube') {
                sonarScanGo(application_name: config.application_name, service_name: config.service_name, sonarSrc: config.sonarSrc)
                }       
        }
}

def sonarScanGo(Map args){
        sh " sonar-scanner -X \
        -Dsonar.projectKey=${args.application_name}-${args.service_name} \
        -Dsonar.sources=${args.sonarSrc} \
        -Dsonar.qualitygate.wait=true \
        -Dsonar.language=go \
        -Dsonar.go.file.suffixes=.go \
        -Dsonar.tests=. \
        -Dsonar.test.inclusions=**/**_test.go \
        -Dsonar.test.exclusions=**/vendor/** \
        -Dsonar.sources.inclusions=**/**.go \
        -Dsonar.exclusions=**/**.xml \
        -Dsonar.go.exclusions=**/*_test.go,**/vendor/**,**/testdata \
        -Dsonar.tests.reportPaths=report-tests.out \
        -Dsonar.go.govet.reportPaths=report-vet.out \
        -Dsonar.go.coverage.reportPaths=coverage.out"
}
