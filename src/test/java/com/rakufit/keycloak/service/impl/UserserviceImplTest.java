package com.rakufit.keycloak.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rakufit.keycloak.service.UserService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserserviceImplTest {
	@Autowired
	private UserService userservice;

	@Test
	public void testTransfer() {
		assertThat(userservice).isNull();
	}
}
