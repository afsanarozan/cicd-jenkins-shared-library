def call() {
  sh "ls -lrth ${WORKSPACE}/"
  Map pipelineCfg = readYaml(file: "${WORKSPACE}/pipeline.yaml")
  return pipelineCfg
}
