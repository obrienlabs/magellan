package global.packet.magellan;

import org.springframework.stereotype.Component;

@Component
public class ApplicationService implements ApplicationServiceLocal {

	@Override
	public String health() {
		return "OK";
	}

	@Override
	public String forward() {
		// TODO Auto-generated method stub
		return "OK";
	}

}
