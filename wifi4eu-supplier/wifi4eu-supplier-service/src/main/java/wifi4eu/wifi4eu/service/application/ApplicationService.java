package wifi4eu.wifi4eu.service.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.common.dto.model.ApplicationDTO;
import wifi4eu.wifi4eu.mapper.application.ApplicationMapper;
import wifi4eu.wifi4eu.repository.application.ApplicationRepository;
import wifi4eu.wifi4eu.service.access_point.AccessPointService;
import wifi4eu.wifi4eu.service.user.UserService;

@Service
public class ApplicationService {
    @Value("${mail.server.location}")
    private String baseUrl;

    @Autowired
    ApplicationMapper applicationMapper;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    UserService userService;

    private final Logger _log = LogManager.getLogger(ApplicationService.class);

    public ApplicationDTO getApplicationBySupplierIdAndRegistrationId(int supplierId, int registrationId) {
        return applicationMapper.toDTO(applicationRepository.findBySupplierIdAndRegistrationId(supplierId, registrationId));
    }
}