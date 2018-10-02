package wifi4eu.wifi4eu.mapper.supplier;

import org.mapstruct.Mapper;
import wifi4eu.wifi4eu.common.dto.model.BankAccountDTO;
import wifi4eu.wifi4eu.entity.supplier.BankAccount;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccountDTO toDTO(BankAccount entity);

    BankAccount toEntity(BankAccountDTO vo);

    List<BankAccountDTO> toDTOList(List<BankAccount> list);

    List<BankAccount> toEntityList(List<BankAccountDTO> list);
}