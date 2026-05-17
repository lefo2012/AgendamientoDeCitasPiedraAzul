package co.edu.unicauca.BackendPiedraAzul.Authentication.keycloak;


import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProvider {

    private final String serverUrl;
    private final String realmName;
    private final String realmMaster;
    private final String adminCli;
    private final String adminUsername;
    private final String adminPassword;
    private final String clientSecret;

    public KeycloakProvider(
            @Value("${keycloak.admin.server-url}") String serverUrl,
            @Value("${keycloak.admin.realm}") String realmName,
            @Value("${keycloak.admin.master-realm}") String realmMaster,
            @Value("${keycloak.admin.client-id}") String adminCli,
            @Value("${keycloak.admin.username}") String adminUsername,
            @Value("${keycloak.admin.password}") String adminPassword,
            @Value("${keycloak.admin.client-secret}") String clientSecret
    ) {
        this.serverUrl = serverUrl;
        this.realmName = realmName;
        this.realmMaster = realmMaster;
        this.adminCli = adminCli;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.clientSecret = clientSecret;
    }

    public RealmResource getRealmResource() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realmMaster)
                .clientId(adminCli)
                .username(adminUsername)
                .password(adminPassword)
                .clientSecret(clientSecret)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();

        return keycloak.realm(realmName);
    }

    public UsersResource getUserResource() {
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }

}