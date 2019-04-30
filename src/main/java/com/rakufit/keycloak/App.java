package com.rakufit.keycloak;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            adminClientTest();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println( "Hello World!" );
    }

    /**
     * need client roles:
     * Client-id:ream-management
     * Associated Role:manage-user,query-user
     */
    public static void adminClientTest(){
        Keycloak kc = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080/auth")
                .realm("demo")
                .username("jiliangchen")
                .password("cjl123456")
                .clientId("admin-cli")
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();

        List<UserRepresentation> users = kc.realm("demo").users().list();
        for(UserRepresentation user:users){
            System.out.println(user.getUsername());
        }

        ClientsResource clients = kc.realm("demo").clients();

        for (ClientRepresentation client : clients.findAll()) {
            if (client.getBaseUrl() != null) {
                System.out.println("baseurl:" + client.getBaseUrl() + "client:" + client.getClientId());
            } else {
                System.out.println("client:" + client.getClientId());
            }
        }
    }
}
