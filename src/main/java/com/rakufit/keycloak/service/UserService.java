package com.rakufit.keycloak.service;

import com.rakufit.keycloak.KeyCloakUser;

public interface UserService {
    public KeyCloakUser transfer(KeyCloakUser user);
}
