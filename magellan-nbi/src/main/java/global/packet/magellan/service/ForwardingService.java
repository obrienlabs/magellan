package global.packet.magellan.service;

public interface ForwardingService {

	String forward(String dnsFrom, String dnsTo, String from, String to, String delay);
	
	String reset();
}
