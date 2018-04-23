package wifi4eu.wifi4eu.mapper.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import wifi4eu.wifi4eu.common.dto.model.ApplicationDTO;
import wifi4eu.wifi4eu.entity.application.Application;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mappings({
            @Mapping(source = "entity.call.id", target = "callId"),
            @Mapping(source = "entity.registration.id", target = "registrationId"),
            @Mapping(source = "entity.supplier.id", target = "supplierId")
    })
    ApplicationDTO toDTO(Application entity);
    @Mappings({
            @Mapping(source = "vo.callId", target = "call.id"),
            @Mapping(source = "vo.registrationId", target = "registration.id")
    })
    Application toEntity(App    licationDTO vo);
    List<ApplicationDTO> toDTOList(List<Application> list);
    List<Application> toEntityList(List<ApplicationDTO> list);
}