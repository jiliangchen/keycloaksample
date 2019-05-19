package com.rakufit.keycloak.service.impl;

import com.rakufit.keycloak.KeyCloakUser;
import com.rakufit.keycloak.service.UserService;

public class UserserviceImpl implements UserService {
    @Override
    public KeyCloakUser transfer(KeyCloakUser user) {
        final KeyCloakUser transformedUser = new KeyCloakUser("X_" +user.getFirstName() + "_X", "X_"+ user.getLastName() + "X");
        return transformedUser;
    }
}
