def call() {
  sh "ls -lrth ${WORKSPACE}/"
  sh "whoami"
  sh "cat ${WORKSPACE}/pipeline.yaml"
  Map pipelineCfg = readYaml(file: "${WORKSPACE}/pipeline.yaml")
  return pipelineCfg
}
