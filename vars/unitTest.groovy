def call() {
  def config = pipelineCfg()
  def envar = checkoutTagging()
  sh 'echo Runnning Unit Testing'
  sh 'ls'

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
                sh "go tool cover -func coverage.out"
                echo sts.toString()
            }

            finally{
                if (fileExists('./report.xml')) { 
                    echo 'junit report'
                    try{
                        junit './report.xml'
                    } catch(e) {
                    }
                }
                if(sts == 1){
                    error('Unit testing Fail!!!!')
                }
            }
        }
}

def goEnv() {
    sh (
        script: '''
        go version

        rm -rf report.xml
        rm -rf cover.out
        rm -rf cover

        export PATH=$PATH:$(go env GOPATH)/bin
        '''
    )
}
