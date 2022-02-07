def call() {
 
  def setting = [:]

  //git configuration
  setting.accesstoken                       = "iUENiKyV6B5hPdoCxV3z"
  setting.tokenapi                          = "glpat-xa3dRUG8-dSg5rY4nNe5"

  //private library configuration   
  setting.go_private_lib                    = "https://gitlab.com/dslt/codesmells.git"
  setting.go_private_lib_auth               = ""

  // jfrog cli
  setting.jfrog_cli                         = "https://getcli.jfrog.io"
  setting.jfrog_user                        = "admin"

  //tools configuration
  setting.artifactory_url                   = "https://klikdevsecops.jfrog.io"
  setting.container_registry_url            = "https://klikdevsecops.jfrog.io/klik-docker"
  setting.registry_credential               = "jfrog-credential"
  setting.sonarqube_enterprise_url          = "https://sq.toolchain.klik.digital"
  setting.sonarqube_developer_url           = "https://sqd.toolchain.klik.digital"

  setting.url_images_registry               = "registry.digitalocean.com/labs-registry"
  setting.token_registry                    = "43366f93440e11863eadc59d62baf4f481a926e6a433c05ebdebc91368fecb13"

  setting.recipient                         = "david.staliat@gmail.com"
  setting.token                             = "token_registry"

  return setting
}
