package global.packet.magellan.model.jpa;

//import javax.persistence.Column;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.MappedSuperclass;
//import javax.xml.bind.annotation.XmlElement;

//@MappedSuperclass
public class IdentifiableDataObject extends DataObject {

//    @Id
//    @Column(name="IDENT_ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)//.AUTO) 
//    @XmlElement
    private Long id;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    

}
