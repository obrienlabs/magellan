package global.packet.magellan.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ForwardingServiceImpl implements ForwardingService {

	@Override
	public String forward() {
		return "OK";
	}

}
