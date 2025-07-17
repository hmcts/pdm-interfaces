#!groovy

@Library("Infrastructure")

def type = "java"
def product = "pdda"
def component = "public-display-manager"
def secrets = [
  'pdda-${env}': [
    secret('public-display-data-aggregator-POSTGRES-USER', 'DB_USER_NAME'),
    secret('public-display-data-aggregator-POSTGRES-PASS', 'DB_PASSWORD'),
    secret('public-display-data-aggregator-POSTGRES-HOST', 'DB_HOST'),
    secret('public-display-data-aggregator-POSTGRES-PORT', 'DB_PORT'),
    secret('public-display-data-aggregator-authentication-clientID', 'PDDA_AZURE_CLIENT_ID'),
    secret('public-display-data-aggregator-authentication-clientSecret', 'PDDA_AZURE_CLIENT_SECRET'),
    secret('public-display-data-aggregator-authentication-tenantID', 'PDDA_AZURE_TENANT_ID'),
    secret('public-display-data-aggregator-authentication-tenantID', 'PDDA_AZURE_REDIRECT_URI'),
    secret('public-display-data-aggregator-pdm-url', 'PDDA_PDM_URL')
  ],
]

withPipeline(type, product, component) {
  loadVaultSecrets(secrets)

 // Vars for Kubernetes
  env.TEST_URL = "https://pdda-public-display-manager-staging.staging.platform.hmcts.net"
  env.DB_NAME = "pdda"
  env.DB_SCHEMA = "pdda"

  afterAlways('smokeTest:stg') {
    steps.archiveArtifacts allowEmptyArchive: true, artifacts: 'smoke-test-report/**/*'
  }
}

static LinkedHashMap<String, Object> secret(String secretName, String envVar) {
    [ $class: 'AzureKeyVaultSecret',
        secretType: 'Secret',
        name: secretName,
        version: '',
        envVariable: envVar
    ]
}
