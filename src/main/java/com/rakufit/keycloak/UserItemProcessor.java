package com.rakufit.keycloak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.rakufit.keycloak.service.UserService;

public class UserItemProcessor implements ItemProcessor<KeyCloakUser, KeyCloakUser> {

    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserItemProcessor.class);

    @Override
    public KeyCloakUser process(final KeyCloakUser user) throws Exception {
        KeyCloakUser transformedUser = userService.transfer(user);
        transformedUser.setUserName(user.getFirstName()+user.getLastName());
        return transformedUser;
    }

}
