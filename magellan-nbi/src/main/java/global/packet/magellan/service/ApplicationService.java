package global.packet.magellan.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService implements ApplicationServiceLocal {

	@Override
	public String health() {
		return "OK";
	}

	@Override
	public String gcp() {
		return "gcp";
	}
	
	@Override
	public String forward() {
		// TODO Auto-generated method stub
		return "OK";
	}

}
