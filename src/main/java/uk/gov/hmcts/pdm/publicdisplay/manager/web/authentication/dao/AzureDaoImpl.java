package uk.gov.hmcts.pdm.publicdisplay.manager.web.authentication.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.authentication.AuthConfigurationProperties;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.authentication.AuthProviderConfigurationProperties;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.authentication.client.OAuthClient;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.authentication.exception.AzureDaoException;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.authentication.model.OAuthProviderRawResponse;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AzureDaoImpl implements AzureDao {

    private final OAuthClient azureActiveDirectoryClient;

    @Override
    public OAuthProviderRawResponse fetchAccessToken(String code,
        AuthProviderConfigurationProperties providerConfig,
        AuthConfigurationProperties configuration) throws AzureDaoException {
        log.debug("Fetching access token(s) for authorization code: {}", code);

        if (StringUtils.isBlank(code)) {
            throw new AzureDaoException("Null code not permitted");
        }

        try {
            HTTPResponse response = azureActiveDirectoryClient.fetchAccessToken(providerConfig,
                configuration.getRedirectUri(), code, configuration.getClientId(),
                configuration.getClientSecret());
            String parsedResponse = response.getContent();

            if (HttpStatus.SC_OK != response.getStatusCode()) {
                throw new AzureDaoException("Unexpected HTTP response code received from Azure",
                    parsedResponse, response.getStatusCode());
            }

            OAuthProviderRawResponse tokenResponse = getTokenResponse(parsedResponse);

            log.debug("Obtained access token for authorization code: {}, {}", code, tokenResponse);
            return tokenResponse;

        } catch (IOException e) {
            throw new AzureDaoException("Failed to fetch Azure AD Access Token", e);
        }
    }

    private OAuthProviderRawResponse getTokenResponse(String jsonObject) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonObject, OAuthProviderRawResponse.class);
    }

}
