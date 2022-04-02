def root_dir = "API-GATEWAY"

  folder("USECASE/${root_dir}/${servicename}") {
        description('Folder containing all jobs for $servicename')
    }

  folder("USECASE/${root_dir}/${servicename}/alpha") {
      description('Folder containing all jobs for alpha')
  }

  folder("USECASE/${root_dir}/${servicename}/beta") {
      description('Folder containing all jobs for alpha')
  }

  folder("USECASE/${root_dir}/${servicename}/release") {
      description('Folder containing all jobs for beta')
  }

  folder("USECASE/${root_dir}/${servicename}/demo") {
      description('Folder containing all jobs for release')
  }



multibranchPipelineJob("USECASE/${root_dir}/${servicename}/alpha/SIT") {
      branchSources {
          git {
              id('123456789') 
              remote("https://$repo")
              credentialsId('automation-auth-token')
              excludes('main')
          }
      }
    factory {
      workflowBranchProjectFactory {
        scriptPath('Jenkinsfile')
      }
    }
    orphanedItemStrategy {
      discardOldItems {
        numToKeep(20)
      }
    }
  }


pipelineJob("USECASE/${root_dir}/${servicename}/demo/demo") {
      properties {
      disableConcurrentBuilds()  
    }
      logRotator {
          numToKeep(7)
        }
      parameters {
        stringParam("BRANCH_NAME", "demo", '')
        stringParam("repository", "${repo}", '')
      }
      triggers {
          gitlabPush {
            buildOnMergeRequestEvents(true) 
            rebuildOpenMergeRequest('source')
          }

        }
      definition {
          cpsScm {
          scm {
            git {
                remote {
                    url("http://${repo}")
                    credentials("automation-auth-token")
                }
                branch('*/demo')
            }
          }

            scriptPath('Jenkinsfile')

        lightweight(true)
        }  

    }
  }

pipelineJob("USECASE/${root_dir}/${servicename}/beta/staging") {
      properties {
      disableConcurrentBuilds()  
    }
      logRotator {
          numToKeep(7)
        }
      parameters {
        stringParam("BRANCH_NAME", "main", '')
        stringParam("repository", "${repo}", '')
      }
      triggers {
          gitlabPush {
            buildOnMergeRequestEvents(true) 
            rebuildOpenMergeRequest('source') 
          }

        }
      definition {
          cpsScm {
          scm {
            git {
                remote {
                    url("http://${repo}")
                    credentials("automation-auth-token")
                }
                branch('*/main')
            }
          }

            scriptPath('Jenkinsfile')

        lightweight(true)
        }  

    }
  }


pipelineJob("USECASE/${root_dir}/${servicename}/release/production") {
      properties {
      disableConcurrentBuilds()  
    }
      logRotator {
          numToKeep(7)
        }
      parameters {
        stringParam("BRANCH_NAME", "main", '')
        stringParam("repository", "${repo}", '')
      }
      triggers {
          gitlabPush {
            buildOnMergeRequestEvents(true) 
            rebuildOpenMergeRequest('source')
          }

        }
      definition {
          cpsScm {
          scm {
            git {
                remote {
                    url("http://${repo}")
                    credentials("automation-auth-token")
                }
                branch('*/main')
            }
          }

            scriptPath('Jenkinsfile')

        lightweight(true)
        }  

    }
  }