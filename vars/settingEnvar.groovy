def call () {
    def envar = [:]


    sh "printenv | sort"

    echo "branch: ${env.gitlabBranch}"
    echo  "action type:  ${env.gitlabActionType}"
    echo  "approval: ${env.gitlabTriggerPhrase}"

    envar.build_number = env.BUILD_NUMBER
    envar.target_branch = env.gitlabTargetBranch


    switch(env.gitlabActionType) {
        case 'PUSH':
            if (env.gitlabBranch == 'main') {
                error "PUSH ON MAIN BRANCH IS DISALLOWED"
                break;
            } else if (env.gitlabBranch == "development") {
                envar.environment = 'sit'
                envar.version     = 'alpha'
                envar.branch      = env.gitlabBranch
                break;
            } else {
                envar.environment = 'none'
                envar.version     = 'nightly-build'
                envar.branch      = env.gitlabBranch
                break;
            }
        case 'MERGE':
                envar.environment = 'staging'
                envar.version     = "beta"
                envar.branch      = env.gitlabBranch
                break;
        case 'TAG_PUSH':
                envar.environment = 'production'
                envar.version     = 'release'
                envar.branch      = env.gitlabBranch
                break;
        default:
            error "not exist"
    }
    echo "envar : ${envar}"
  return envar
}


//ideal workflow
/*
SWITCH GITLABACTIONTYPE
    CASE PUSH
        IF BRANCH MAIN
            MESSAGE: PUSH ON MAIN BRANCH IS DISALLOWED
        IF BRANCH DEVELOPMENT
            FILL ENVAR (VERSION: ALPHA, ENVIRONMENT: DEV/SIT)
        ELSE
            FILL ENVAR (VERSION: NIGHTLY-BUILD, ENVIRONMENT: NONE, BRANCH: $GITLABBRANCH)
    CASE MERGED
        FILL ENVAR (VERSION: BETA, ENVIRONMENT: STAGING, BRANCH: $GITLABTARGETBRANCH/MAIN)
        MERGED BRANCH DEVELOPMENT TO MAIN
    CASE TAG_PUSH
        FILL ENVAR (VERSION: RELEASE, ENVIRONMENT: PRODUCTION, BRANCH: MAIN )
    DEFAULT
        EXIT
*/

  /* switch(env.gitlabTargetBranch) {
        case 'main':
            if (env.gitlabActionType == 'MERGE' || env.gitlabActionType == 'PUSH') {
                echo "comment approved harusnya jalanin block ini"
                envar.environment = 'staging'
                envar.version     = "beta"
                envar.branch      = env.gitlabBranch
            }else if(gitlabActionType == 'TAG_PUSH') {
                envar.environment = 'production'
                envar.version     = "release"
                envar.branch      = env.gitlabSourceBranch
            }
            break; 
        case 'development': 
            envar.environment = 'sit'
            envar.version     = "alpha"
            envar.branch      = "development"
            break; 
        default: 
            envar.environment = 'dev'
            envar.branch      = env.gitlabBranch
            break;
    } */
