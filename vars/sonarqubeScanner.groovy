def call(Map envar) {
        echo "Running Code Review With SonarQube"
        withSonarQubeEnv(credentialsId: "${envar.credential_sonarqube}", installationName: 'sonarqube') {
                sonarScanGo(name_space: envar.name_space, service_name: envar.service_name, sonarSrc: envar.sonarSrc)
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
