package global.packet.magellan.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import global.packet.magellan.model.jaxb.PayloadRequest;
import global.packet.magellan.model.jaxb.PayloadRequestImpl;
import global.packet.magellan.model.jpa.PayloadEntity;
import global.packet.magellan.model.jpa.PayloadEntityImpl;

@Mapper
public interface PayloadRequestMapper {

	@Mapping(source = "message", target = "text")
	PayloadEntityImpl payloadRequestToPayloadEntity(PayloadRequest payloadRequest);
}
