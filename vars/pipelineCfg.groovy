def call() {
  sh "ls -lrth ${WORKSPACE}/"
  sh "cat ${WORKSPACE}/pipeline.yaml"
  Map pipelineCfg = readYaml(file: "${WORKSPACE}/pipeline.yaml")
  return pipelineCfg
}
