package global.packet.magellan;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import global.packet.magellan.mapper.PayloadRequestMapper;
import global.packet.magellan.mapper.PayloadRequestMapperImpl;
import global.packet.magellan.model.jaxb.PayloadRequest;
import global.packet.magellan.model.jaxb.PayloadRequestImpl;
import global.packet.magellan.model.jpa.PayloadEntity;

import java.time.LocalDate;

import org.junit.Assert;

public class ModelMapperTest {

	//@InjectMocks 
	// manual for now
	PayloadRequestMapper payloadRequestMapper = new PayloadRequestMapperImpl();
	
	
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);

	}
	
	@Test
	public void testMapper() {
		LocalDate now = LocalDate.now();
		LocalDate end = now.withDayOfMonth(1).plusMonths(1);
		LocalDate end2 = now.withDayOfMonth(now.lengthOfMonth()).plusMonths(1);
		
		
		String expected = "a Message";
		PayloadRequest request = new PayloadRequestImpl();
		request.setId(1L);
		request.setSequence(1L);
		request.setMessage(expected);
		
		
		PayloadEntity entity = payloadRequestMapper.payloadRequestToPayloadEntity(request);
		Assert.assertNotNull(entity);
		Assert.assertEquals(expected, entity.getText());
	}
}
