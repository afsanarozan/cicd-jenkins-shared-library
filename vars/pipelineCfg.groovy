def call() {
  sh "ls -lrth ${WORKSPACE}/"
  echo "${env.STAGE_NAME}"
  sh "cat ${WORKSPACE}/pipeline.yaml"
  Map pipelineCfg = readYaml(file: "${WORKSPACE}/pipeline.yaml")
  return pipelineCfg
}
