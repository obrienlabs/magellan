package global.packet.magellan;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import global.packet.magellan.controller.ApplicationServiceController;

public class ApplicationServiceControllerTest {
	// controller is unmocked, but service bean inside is mocked
	private @InjectMocks ApplicationServiceController controller 
		= Mockito.mock(ApplicationServiceController.class);
	// if controller is InjectMocked then Mock service bean will be injected
	//@Mock ForwardingController controller;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testForwardRequest() {
		String expected = "ok";
		Mockito.when(controller.getHealth())
			.thenReturn(expected);
		String response = controller.getHealth();
		Assertions.assertNotNull(response);
		Assert.assertEquals(expected, response);
	}

	
}



