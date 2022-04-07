def call(Map envar) {
  
  echo "Running Code Review with SonarQube"
  def scanEnvar = [:]
  echo "environment is ${envar.environment}"
  echo "${envar.language}"
  
  scanEnvar.scannerHome = tool "sonarscanner"
    if(envar.environment == 'sit' || envar.environment  == 'staging' || envar.environment  == 'production'){
        //for community used
        /*
        withSonarQubeEnv ("sonarcommunity") {
            project_key = "${envar.project_key}"
            sonarScanGoCommunity(scannerHome: scannerHome, project_key: project_key, project_version: "${envar.version}")
        }
        */

        //sq enterprise
        withSonarQubeEnv ("sonarenterprise") {

            scanEnvar.project_key = "${envar.project_key}"
            scanEnvar.project_version = "${envar.version}"
            scanEnvar.branch = "${envar.environment}"
        
            switch(envar.language) {
                case "golang":   
                    sonarScanGoEnterpriseGo(scanEnvar)
                    break;
                case "node":
                    sonarScanGoEnterpriseNode(scanEnvar)
                    break;
                case "maven":
                    sonarScanMvn(scanEnvar)
                    break;
                }
            }

            timeout(time: 10, unit: 'MINUTES') {
                def qg = waitForQualityGate(webhookSecretId: 'webhook-secret')
                if (qg.status != 'OK') {
                    error "Pipeline aborted due to quality gate failure: ${qg.status}"
                }
            }
    }
}

def sonarScanGoCommunity(Map args) {
    sh "${args.scannerHome}/bin/sonar-scanner -X \
    -Dsonar.projectKey=${args.project_key}\
    -Dsonar.projectVersion=${args.project_version}\
    -Dsonar.sources=. \
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

def sonarScanGoEnterpriseNode(Map args) {
    sh "${args.scannerHome}/bin/sonar-scanner -X \
    -Dsonar.projectKey=${args.project_key}\
    -Dsonar.projectVersion=${args.project_version}\
    -Dsonar.branch.name=${args.branch} \
    -Dsonar.sources=. \
    -Dsonar.css.node=. "
}

def sonarScanGoEnterpriseGo(Map args) {
    container('golang') {
        sh "go test ./... -coverprofile=coverage.out"
    }

    sh "${args.scannerHome}/bin/sonar-scanner -X \
    -Dsonar.projectKey=${args.project_key}\
    -Dsonar.projectVersion=${args.project_version}\
    -Dsonar.branch.name=${args.branch} \
    -Dsonar.sources=. \
    -Dsonar.go.file.suffixes=.go \
    -Dsonar.tests=. \
    -Dsonar.test.inclusions=**/**_test.go,**_test.go \
    -Dsonar.test.exclusions=**/vendor/** \
    -Dsonar.sources.inclusions=**/**.go \
    -Dsonar.exclusions=**/**.xml \
    -Dsonar.go.exclusions=**/*_test.go,**_test.go,**/vendor/**,**/testdata \
    -Dsonar.tests.reportPaths=report-tests.out \
    -Dsonar.go.govet.reportPaths=report-vet.out \
    -Dsonar.go.coverage.reportPaths=coverage.out"
}

def sonarScanNodeJS(Map args) {
    sh "${args.scannerHome}/bin/sonar-scanner -X \
    -Dsonar.projectKey=${args.project_key}\
    -Dsonar.projectVersion=${args.project_version}\
    -Dsonar.branch.name=${args.branch} \
    -Dsonar.sources=src \
    -Dsonar.test.inclusions=src/**/*.spec.js\
    -Dsonar.test.exclusions=src/**/*.spec.js,src/**/*.mock.js,node_modules/*,coverage/lcov-report/* \
    -Dsonar.javascript.lcov.reportPaths=coverage/lcov.info \
    -Dsonar.testExecutionReportPaths=test-report.xml"
}

def sonarScanMvn(Map args) {
    sh "${args.scannerHome}/bin/sonar-scanner -X \
    -Dsonar.projectKey=${args.project_key}\
    -Dsonar.projectVersion=${args.project_version}\
    -Dsonar.branch.name=${args.branch} \
    -Dsonar.source=. \
    -Dsonar.java.source=8 \
    -Dsonar.language=java \
    -Dsonar.java.binaries=. "
}