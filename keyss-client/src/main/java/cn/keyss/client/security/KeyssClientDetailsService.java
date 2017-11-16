package cn.keyss.client.security;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.util.HashMap;
import java.util.Map;

/***
 * Oauth客户详情服务
 */
public class KeyssClientDetailsService implements ClientDetailsService {

    private Map<String, ClientDetails> clientDetailsStore = new HashMap<String, ClientDetails>();

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails details = clientDetailsStore.get(clientId);
        if (details == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        return details;
    }

    public void setClientDetailsStore(Map<String, ? extends ClientDetails> clientDetailsStore) {
        this.clientDetailsStore = new HashMap<String, ClientDetails>(clientDetailsStore);
    }

}