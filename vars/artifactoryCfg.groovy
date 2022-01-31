def call() {
    def setting = settings()
        def token = credentials('access-token')

        environment {
            GOPROXY = "https://${token}@klikdevsecops.jfrog.io/artifactory/api/go/klik-go-remote"
        }    

        container('golang') {
            echo 'build go binary'
            sh ''
            sh "printenv | sort"
            sh "go build"
        }
}

