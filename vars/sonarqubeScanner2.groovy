def call(Map envar) {
  def config = pipelineCfg()
  def setting = checkoutTagging()

    echo "Running Code Review With SonarQube"
        echo "${config.service_name}"
        withSonarQubeEnv(credentialsId: "${config.credential_sonarqube}", installationName: 'sonarqube') {
                sonarScanGo(name_space: config.name_space, service_name: config.service_name, sonarSrc: config.sonarSrc)
        }
         
}

def sonarScanGo(Map args){
        sh " sonar-scanner -X \
        -Dsonar.projectKey=${args.name_space}-${args.service_name} \
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
