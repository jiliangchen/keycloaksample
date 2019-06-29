package com.rakufit.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class Application {
    public static String KEYCLOAK_USER = "";
    public static String KEYCLOAK_PASSWORD = "";

    public static void main(String[] args) throws Exception {
        java.io.Console console = System.console();

        //KEYCLOAK_USER = console.readLine("Username: ");
        //KEYCLOAK_PASSWORD = new String(console.readPassword("Password: "));

        SpringApplication.run(Application.class, args);
    }
}
