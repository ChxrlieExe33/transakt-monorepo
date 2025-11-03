package com.cdcrane.customers.service;

import com.cdcrane.customers.event.CustomerVerifiedEvent;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param event User registration event.
     */
    public void createUserAccount(CustomerVerifiedEvent event) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        try {
            // Step 1: Create a user with attributes only
            // Had to split this into steps while debugging to figure out an issue, will convert back after.
            UserRepresentation user = buildUserWithAttributes(event);
            Response response = usersResource.create(user);

            if (response.getStatus() != 201) {
                log.error("Failed to create keycloak user account for {}. Status code: {}", event.email(), response.getStatus());
                response.close();
                return;
            }

            String userId = CreatedResponseUtil.getCreatedId(response);
            response.close();

            log.info("Keycloak user account for {} created successfully with ID {}.", event.email(), userId);

            // Step 2: Set a temporary password and required actions
            UserRepresentation passwordUpdate = new UserRepresentation();

            CredentialRepresentation tempPassword = new CredentialRepresentation();
            tempPassword.setType(CredentialRepresentation.PASSWORD);
            tempPassword.setTemporary(true);
            tempPassword.setValue(event.temporaryPassword());

            passwordUpdate.setCredentials(List.of(tempPassword));
            passwordUpdate.setRequiredActions(List.of("UPDATE_PASSWORD"));

            usersResource.get(userId).update(passwordUpdate);

            log.info("Temporary password and required actions set for user {}.", userId);

        } catch (Exception ex) {
            log.error("Failed to create keycloak user account for {}. Error: {}", event.email(), ex.getMessage());
        }
    }

    /**
     * Prepare the first stage UserRepresentation with details, and the Customer ID attribute.
     * @param event The CustomerVerifiedEvent with the necessary details.
     * @return A minimal UserRepresentation.
     */
    private UserRepresentation buildUserWithAttributes(CustomerVerifiedEvent event) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(event.email());
        user.setEmail(event.email());
        user.setEmailVerified(true);
        user.setFirstName(event.firstName());
        user.setLastName(event.lastName());
        user.setEnabled(true);

        // Set the customerId attribute here
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("customerId", List.of(String.valueOf(event.customerId())));
        user.setAttributes(attributes);

        return user;
    }


}
