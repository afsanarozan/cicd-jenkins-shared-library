def call() {
  sh "ls -lrth ${WORKSPACE}/"
  wh "cat ${WORKSPACE}/pipeline.yaml"
  Map pipelineCfg = readYaml(file: "${WORKSPACE}/pipeline.yaml")
  return pipelineCfg
}
