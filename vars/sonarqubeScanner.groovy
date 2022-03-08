def call(Map envar) {
def config = pipelineCfg()
        container('sonarscanner'){
        echo "Running Code Review With SonarQube"
                withSonarQubeEnv(credentialsId: "sonarqube-ee-token", installationName: 'sonarqube') {
                sonarScanGo(name_space: config.name_space, service_name: config.service_name, sonarSrc: config.sonarSrc)
                }       
        }
}

def sonarScanGo(Map args){
        sh " sonar-scanner -X \
        -Dsonar.host.url=http://sonarqube-ee.toolchains.atklik.xyz \
        -Dsonar.login=c13cec4b07228eab94869950ac5d972386168524 \
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
