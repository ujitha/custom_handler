package org.wso2.customhandler;

import org.apache.synapse.MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.wso2.carbon.apimgt.gateway.APIMgtGatewayConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;

import java.util.HashMap;
import java.util.Set;
import java.util.StringJoiner;

public class CustomOrganizationLevelThrottleHandler extends AbstractHandler {

    private static final String CUSTOM_PROPERTY_GROUP_IDS = "groupIds";


    public boolean handleRequest(MessageContext messageContext) {

        AuthenticationContext authenticationContext = APISecurityUtils.getAuthenticationContext(messageContext);
        Set<String> groupIdsSet = authenticationContext.getApplicationGroupIds();

        StringJoiner stringJoiner = new StringJoiner("|");
        groupIdsSet.forEach(e -> stringJoiner.add(e));
        String groupIds = stringJoiner.toString();

        HashMap<String, Object> customProperties = (HashMap<String, Object>) messageContext.getProperty(APIMgtGatewayConstants.CUSTOM_PROPERTY);

        if (customProperties == null) {
            customProperties = new HashMap<String, Object>();
        }
        customProperties.put(CUSTOM_PROPERTY_GROUP_IDS, groupIds);

        messageContext.setProperty(APIMgtGatewayConstants.CUSTOM_PROPERTY, customProperties);
        return true;
    }

    public boolean handleResponse(MessageContext messageContext) {
        return true;
    }
}
