package global.packet.magellan.service;

public interface ApplicationServiceLocal {

	String health();
	String gcpViaEnv();
	String gcpViaFile();
}
