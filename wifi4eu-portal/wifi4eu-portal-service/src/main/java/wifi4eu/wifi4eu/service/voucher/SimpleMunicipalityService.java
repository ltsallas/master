package wifi4eu.wifi4eu.service.voucher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.common.dto.model.SimpleMunicipalityDTO;
import wifi4eu.wifi4eu.mapper.voucher.SimpleMunicipalityMapper;
import wifi4eu.wifi4eu.repository.voucher.SimpleMunicipalityRepository;

import java.util.List;

@Service
public class SimpleMunicipalityService {


    @Autowired
    SimpleMunicipalityMapper municipalityMapper;

    @Autowired
    SimpleMunicipalityRepository municipalityRepository;

    public List<SimpleMunicipalityDTO> getAllMunicipalities() {
        return municipalityMapper.toDTOList(municipalityRepository.findAllMunicipalitiesFromApplications());
    }
}
