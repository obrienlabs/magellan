package global.packet.magellan.service;

public interface ApplicationServiceLocal {

	String health();
	String event();
	String gcpViaEnv();
	String gcpViaFile();
}
