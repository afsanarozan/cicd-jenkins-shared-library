def call() {
 
  def setting = [:]

  //git configuration
  setting.do_url                            = "registry.digitalocean.com"
  setting.do_token_registry                 = "d5436ff5c04a514a24a86ac0415b7758a0ee3449835ad496fc516b61b2504f0b"
  setting.accesstoken                       = "iUENiKyV6B5hPdoCxV3z"
  setting.tokenapi                          = "glpat-xa3dRUG8-dSg5rY4nNe5"

  setting.url_images_registry               = "registry.digitalocean.com/labs-registry"
  setting.token_registry                    = "43366f93440e11863eadc59d62baf4f481a926e6a433c05ebdebc91368fecb13"

  setting.recipient                         = "david.staliat@gmail.com"
  setting.token                             = "token_registry"
  setting.credential                        = "nonprod-cluster"
  setting.DO_nonprod_cluster                = "do-sgp1-labs-nonproduction "

  return setting
}
