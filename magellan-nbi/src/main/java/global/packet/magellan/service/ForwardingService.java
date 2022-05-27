package global.packet.magellan.service;

public interface ForwardingService {

	String forward(String dnsFrom, String dnsTo, String from, String to, String delay);
	
    String traffic(String dns, String to, String delay, String iterationsString);

	String reset();
}
