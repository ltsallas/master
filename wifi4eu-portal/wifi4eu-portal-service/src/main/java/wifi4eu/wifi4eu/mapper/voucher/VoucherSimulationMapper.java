package wifi4eu.wifi4eu.mapper.voucher;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import wifi4eu.wifi4eu.common.dto.model.*;
import wifi4eu.wifi4eu.entity.municipality.Municipality;
import wifi4eu.wifi4eu.entity.registration.Registration;
import wifi4eu.wifi4eu.entity.voucher.VoucherSimulation;
import wifi4eu.wifi4eu.mapper.municipality.MunicipalityMapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoucherSimulationMapper {

    @Mappings({
            @Mapping(source = "entity.municipality", target = "municipality"),
            @Mapping(source = "entity.voucherAssignment.id", target = "voucherAssignment")
    })
    VoucherSimulationDTO toDTO(VoucherSimulation entity);
    @Mappings({
            @Mapping(source = "vo.municipality", target = "municipality"),
            @Mapping(source = "vo.voucherAssignment", target = "voucherAssignment.id")
    })
    VoucherSimulation toEntity(VoucherSimulationDTO vo);
    List<VoucherSimulationDTO> toDTOList(List<VoucherSimulation> list);
    List<VoucherSimulation> toEntityList(List<VoucherSimulationDTO> list);

    MunicipalityDTO municipalityToMunicipalityDTO(Municipality entity);
    Municipality municipalityDTOtoMunicipality(MunicipalityDTO dto);

    @Mappings({
            @Mapping(source = "entity.user.id", target = "userId", ignore = true),
            @Mapping(source = "entity.municipality.id", target = "municipalityId")
    })
    RegistrationDTO toDTO(Registration entity);
    @Mappings({
            @Mapping(source = "vo.userId", target = "user.id", ignore = true),
            @Mapping(source = "vo.municipalityId", target = "municipality.id")
    })
    Registration toEntity(RegistrationDTO vo);

}
