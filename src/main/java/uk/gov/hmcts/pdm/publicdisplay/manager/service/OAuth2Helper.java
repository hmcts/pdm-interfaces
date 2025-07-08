package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.hmcts.pdm.publicdisplay.initialization.InitializationService;

/**
 * Title: OAuth2Helper.
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: CGI
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class OAuth2Helper {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2Helper.class);
    private static final String AZURE_TENANT_ID =
        "spring.cloud.azure.active-directory.profile.tenant-id";
    private static final String AZURE_CLIENT_ID =
        "spring.cloud.azure.active-directory.credential.client-id";
    private static final String AZURE_CLIENT_SECRET =
        "spring.cloud.azure.active-directory.credential.client-secret";

    // Values from application.properties
    private final String tenantId;
    private final String clientId;
    private final String clientSecret;

    public OAuth2Helper() {
        this(InitializationService.getInstance().getEnvironment());
    }

    protected OAuth2Helper(Environment env) {
        this.tenantId = env.getProperty(AZURE_TENANT_ID);
        this.clientId = env.getProperty(AZURE_CLIENT_ID);
        this.clientSecret = env.getProperty(AZURE_CLIENT_SECRET);
    }

    public String getTenantId() {
        LOG.debug("getTenantId()");
        return tenantId;
    }
    
    public String getClientId() {
        LOG.debug("getClientId()");
        return clientId;
    }
    
    public String getClientSecret() {
        LOG.debug("getClientSecret()");
        return clientSecret;
    }
}
