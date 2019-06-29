package com.rakufit.keycloak;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.batch.item.ItemWriter;

public class KeyCloakUserItemWriter implements ItemWriter<KeyCloakUser> {
    Keycloak keycloak;
    
    private static final Logger log = LoggerFactory.getLogger(KeyCloakUserItemWriter.class);

    public KeyCloakUserItemWriter(Keycloak kc){
        keycloak = kc;
    }

    @Override
    public void write(List<? extends KeyCloakUser> items) throws Exception {
        String realm = "demo";
        for(KeyCloakUser user:items){
            log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~KeyCloakUser:" + user);

            // Define user
            UserRepresentation ur = new UserRepresentation();
            ur.setEnabled(true);
            ur.setUsername(user.getUserName());
            ur.setFirstName(user.getFirstName());
            ur.setLastName(user.getLastName());

            // Get realm
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource userRessource = realmResource.users();

            // Create user (requires manage-users role)
            //Response response = userRessource.create(ur);
            //System.out.println("Repsonse: " + response.getStatusInfo());
        }
    }
}
