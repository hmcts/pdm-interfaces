module "pdm_key_vault" {
  source = "git@github.com:hmcts/cnp-module-key-vault?ref=master"

  name                    = "${var.product}-${var.env}"
  product                 = var.product
  env                     = var.env
  object_id               = var.jenkins_AAD_objectId
  resource_group_name     = azurerm_resource_group.pdm_resource_group.name
  product_group_name      = "PDM"
  create_managed_identity = true

  common_tags = var.common_tags
}
