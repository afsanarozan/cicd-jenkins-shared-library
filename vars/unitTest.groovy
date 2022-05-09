def call() {
//  def envar = checkoutTagging()
  sh 'echo Runnning Unit Testing'
  sh 'ls'
  def test = [:]
  def root = tool type: 'go', name: 'Go'
  withEnv(["GOROOT=${root}", "PATH+GO=${root}/bin"]) {
        sh 'go version'
        goEnv()

        def sts = 1
            try {
                sts = sh (
                    returnStatus: true, 
                    script: '''
                    export PATH=$PATH:$(go env GOPATH)/bin
                    CGO_ENABLED=0 go test ./... -v -coverprofile coverage.out 2>&1 | \
                        go-junit-report -set-exit-code > ./report.xml
                    echo $?
                    '''
                )
                sh "touch coverage.out"
                sh "cat report.xml"
                sh "cat coverage.out"
                sh "go tool cover -func=coverage.out"
                echo sts.toString()
            }

             finally{
                if (fileExists('./report.xml')) { 
                    echo 'junit report'
                    try{
                        sh 'cat ./report.xml'
                    } catch(e) {
                    }
                }
                if(sts == 2){
                    error('Unit testing Fail!!!!')
                }
             }

             def unitTestGetValue = sh(returnStdout: true, script: 'go tool cover -func=coverage.out | grep total | sed "s/[[:blank:]]*$//;s/.*[[:blank:]]//"')
             def unitTest_score   = "Your score is ${unitTestGetValue}"
             echo "${unitTest_score}" 

             test.score = "${unitTestGetValue}"

             return test
        }
}

def goEnv() {
    sh (
        script: '''
        go version

        rm -rf report.xml
        rm -rf cover.out
        rm -rf coverage.out
        rm -rf cover

        export PATH=$PATH:$(go env GOPATH)/bin

        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kds-platform/plugin.git".insteadOf "https://gitlab.com/kds-platform/plugin.git"
        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kds-platform/library".insteadOf "https://gitlab.com/kds-platform/library"
        
        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kds-platform/apim/notification-service".insteadOf "https://gitlab.com/kds-platform/apim/notification-service.git"

        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kliklab/jose-ported.git".insteadOf "https://gitlab.com/kliklab/jose-ported.git"

        git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kliklab/libs.git".insteadOf "https://gitlab.com/kliklab/libs.git"
        
        go mod tidy -e

        go get -u golang.org/x/lint/golint
        golint -set_exit_status ./controller/...
        
        go get -u github.com/jstemmer/go-junit-report
        go clean -testcache
        '''
    )
}
