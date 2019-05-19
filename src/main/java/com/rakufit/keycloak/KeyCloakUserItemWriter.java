package com.rakufit.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.batch.item.ItemWriter;

import javax.ws.rs.core.Response;
import java.util.List;

public class KeyCloakUserItemWriter implements ItemWriter<KeyCloakUser> {
    Keycloak keycloak;

    public KeyCloakUserItemWriter(Keycloak kc){
        keycloak = kc;
    }

    @Override
    public void write(List<? extends KeyCloakUser> items) throws Exception {
        String realm = "demo";
        for(KeyCloakUser user:items){
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~KeyCloakUser:" + user);

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
