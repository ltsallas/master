package wifi4eu.wifi4eu.service.municipality;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.common.dto.model.MunicipalityDTO;
import wifi4eu.wifi4eu.mapper.municipality.MunicipalityMapper;
import wifi4eu.wifi4eu.repository.municipality.MunicipalityRepository;

import java.util.List;

@Service("publicMunicipalityService")
public class MunicipalityService {
    @Autowired
    MunicipalityMapper municipalityMapper;

    @Autowired
    MunicipalityRepository municipalityRepository;

    public MunicipalityDTO getMunicipalityById(int municipalityId) {
        return municipalityMapper.toDTO(municipalityRepository.findOne(municipalityId));
    }

    @Cacheable(value = "publicGetMunicipalitiesCountGroupedByLauId")
    public List<Object> getMunicipalitiesCountGroupedByLauId() {
        return Lists.newArrayList(municipalityRepository.findMunicipalitiesCountGroupedByLauId());
    }

}