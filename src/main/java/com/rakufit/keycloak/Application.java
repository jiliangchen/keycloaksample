package com.rakufit.keycloak;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static String KEYCLOAK_USER = "";
    public static String KEYCLOAK_PASSWORD = "";

    public static void main(String[] args) throws Exception {
        java.io.Console console = System.console();

        //KEYCLOAK_USER = console.readLine("Username: ");
        //KEYCLOAK_PASSWORD = new String(console.readPassword("Password: "));

        SpringApplication.run(Application.class, args);
    }

    @Bean
    Keycloak keycloak() {
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
        return kc;
    }
}
