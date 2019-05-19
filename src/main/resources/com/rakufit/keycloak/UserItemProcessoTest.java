package com.rakufit.keycloak;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.rakufit.keycloak.service.UserService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserItemProcessoTest {

	@InjectMocks
	private UserItemProcessor processor = new UserItemProcessor();

	@Mock
	private UserService userService;

	@Test
	public void testProcessor() throws Exception {
		KeyCloakUser userParam = new KeyCloakUser();
		userParam.setFirstName("jiliang");
		userParam.setLastName("chen");

		KeyCloakUser userReturn = new KeyCloakUser();
		userReturn.setFirstName("X_jiliang_X");
		userReturn.setLastName("X_chen_X");

		when(this.userService.transfer(userParam)).thenReturn(userReturn);
		KeyCloakUser actual = processor.process(userParam);
		
		verify(this.userService, times(1)).transfer(userParam);
		assertThat(actual.getFirstName()).isEqualTo(userReturn.getFirstName());
		assertThat(actual.getLastName()).isEqualTo(userReturn.getLastName());
		String actureUserName = actual.getUserName();
		String returnUserName = userReturn.getUserName();
		assertThat(actureUserName).isEqualTo(returnUserName);
	}
}
