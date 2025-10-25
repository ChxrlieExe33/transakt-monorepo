package com.cdcrane.customers.service;

import com.cdcrane.customers.event.CustomerRegisteredEvent;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KeycloakUserManagerService {

    @Value("${keycloak.url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private Keycloak keycloak;

    @PostConstruct
    public void init() {

        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    /**
     * Initial implementation of the functionality to register an application user
     * into keycloak for OAuth and OIDC.
     * *
     * IMPORTANT: For now this method is being run immediately after registering a user,
     * later this should run after the user has verified their email on our application side.
     * @param event User registration event.
     */
    public void createUserAccount(CustomerRegisteredEvent event) {

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = buildUserAndCredentials(event);

        try {
            Response response = usersResource.create(user);

            if (response.getStatus() != 201) {
                response.close();

                log.error("Failed to create keycloak user account for {}. Status code: {}", event.email(), response.getStatus());
                return;
            }

            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            response.close();

            log.info("Keycloak user account for {} with id {} and temporary password created successfully.", userId, event.email());

        } catch (Exception ex) {
            log.error("Failed to create keycloak user account for {}. Error: {}", event.email(), ex.getMessage());
            return;
        }

    }

    private UserRepresentation buildUserAndCredentials(CustomerRegisteredEvent event) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(event.email());
        user.setEmail(event.email());
        user.setEmailVerified(true);
        user.setFirstName(event.firstName());
        user.setLastName(event.lastName());
        user.setEnabled(true);

        CredentialRepresentation tempPassword = new CredentialRepresentation();
        tempPassword.setType(CredentialRepresentation.PASSWORD);
        tempPassword.setTemporary(true);

        // TODO: Either receive a password from the user, or randomly generate a password and give it to them.
        tempPassword.setValue("test123");

        user.setCredentials(List.of(tempPassword));
        user.setRequiredActions(List.of("UPDATE_PASSWORD"));
        return user;
    }

}
