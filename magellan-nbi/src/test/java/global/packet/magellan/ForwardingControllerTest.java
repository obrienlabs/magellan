package global.packet.magellan;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import global.packet.magellan.controller.ForwardingController;

public class ForwardingControllerTest {
	// controller is unmocked, but service bean inside is mocked
	private @InjectMocks ForwardingController controller = Mockito.mock(ForwardingController.class);
	// if controller is InjectMocked then Mock service bean will be injected
	//@Mock ForwardingController controller;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
//	@Test
	public void testForwardRequest() {
//		String expected = "ok";
//		Mockito.when(controller.getPacket())
//			.thenReturn(expected);
//		String response = controller.getPacket();
//		Assertions.assertNotNull(response);
//		Assert.assertEquals(expected, response);
	}

	
}



