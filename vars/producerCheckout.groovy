
def call() {
    checkout(
          [
              $class: 'GitSCM',
              branches: [
                  [name: "${env.gitlabSourceBranch}"]
                  ],
              extensions: [],
              userRemoteConfigs: [
                  [credentialsId: 'automation-auth-token',
                    url: "${env.gitlabSourceRepoHomepage}"]]])
    sh 'ls -lh'
}
