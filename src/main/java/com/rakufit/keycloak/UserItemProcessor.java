package com.rakufit.keycloak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<KeyCloakUser, KeyCloakUser> {

    private static final Logger log = LoggerFactory.getLogger(UserItemProcessor.class);

    @Override
    public KeyCloakUser process(final KeyCloakUser user) throws Exception {
        final KeyCloakUser transformedUser = new KeyCloakUser(user.getFirstName(), user.getLastName());
        transformedUser.setUserName(user.getFirstName()+user.getLastName());
        log.info("Converting (" + user + ") into (" + transformedUser + ")");

        return transformedUser;
    }

}
