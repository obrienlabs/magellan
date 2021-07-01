package global.packet.magellan.model.jaxb;

public class PayloadResponseImpl implements PayloadResponse {

	private Long id;
	private Long sequence;
	private String message;
	
	@Override
	public Long getSequence() {
		return sequence;
	}

	@Override
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

}
