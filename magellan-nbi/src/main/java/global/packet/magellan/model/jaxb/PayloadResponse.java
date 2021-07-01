package global.packet.magellan.model.jaxb;

public interface PayloadResponse {

	Long getSequence();
	void setSequence(Long sequence);
	Long getId();
	void setId(Long id);
	String getMessage();
	void setMessage(String message);
}
