package global.packet.magellan.model.jpa;

public interface PayloadEntity {

	Long getId();
	void setId(Long id);
	
	Long getVersion();
	void setVersion(Long version);
	
	String getText();
	void setText(String text);
}
