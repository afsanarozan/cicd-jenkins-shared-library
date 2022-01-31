def call() {
    def envar = [:]
  sh "printenv | sort"

switch(env.BRANCH_NAME) {
    case 'master':
        envar.branch      = "master"
        break; 
    case 'sit': 
      envar.branch      = "sit"
      break; 
    case 'main': 
      envar.branch      = "main"
      break; 
  }
  
  tag(envar.branch)
  
  return envar
  
}

def tag (branch) {
  withCredentials([usernamePassword(credentialsId: 'gitlab-auth-token', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
      sh '''
      date=$(date '+%Y%m%d')
      echo "$BUILD_NUMBER"
      git config --global user.email "adekuniawan1999@gmail.com"
      git config --global user.name "adekurniawan1999"
      git clone https://${USERNAME}:${PASSWORD}@${repo};
      cd ${application_name};
      git checkout main;
      git log;
      git fetch --tags;
      git tag -a release-${date}-$BUILD_NUMBER ${commit_id} -m "release-${date}-$BUILD_NUMBER-${commit_id}";
      git rev-parse release-${date}-$BUILD_NUMBER ${commit_id};
      git push origin release-${date}-$BUILD_NUMBER;
    '''
     
   }

}
