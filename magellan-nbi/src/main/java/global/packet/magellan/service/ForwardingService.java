package global.packet.magellan.service;

public interface ForwardingService {

	String forward(String dnsFrom, String dnsTo, String from, String to, String delay, String region);
	
    String traffic(String useCaseNumber, String client, String chaosPercentage, String dns, String to, String delay, String iterationsString, String region);

	String reset();
}
