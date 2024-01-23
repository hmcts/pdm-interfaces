variable "product" {}

variable "component" {}

variable "location" {
  default = "UK South"
}

variable "env" {}

variable "subscription" {}

variable "common_tags" {
  type = map(string)
}

# Private DNS zone configuration (for postgres)
variable "dns_resource_group" {
  default = "core-infra-intsvc-rg"
}

variable "private_dns_zone" {
  default = "private.postgres.database.azure.com"
}


