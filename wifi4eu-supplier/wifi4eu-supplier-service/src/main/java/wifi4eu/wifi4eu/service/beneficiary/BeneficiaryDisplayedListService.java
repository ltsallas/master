package wifi4eu.wifi4eu.service.beneficiary;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wifi4eu.wifi4eu.common.cns.CNSManager;
import wifi4eu.wifi4eu.common.dto.model.SupplierDTO;
import wifi4eu.wifi4eu.common.dto.model.UserDTO;
import wifi4eu.wifi4eu.common.dto.rest.ErrorDTO;
import wifi4eu.wifi4eu.common.dto.rest.ResponseDTO;
import wifi4eu.wifi4eu.common.ecas.UserHolder;
import wifi4eu.wifi4eu.common.security.UserContext;
import wifi4eu.wifi4eu.entity.registration.Registration;
import wifi4eu.wifi4eu.mapper.beneficiary.BeneficiaryDisplayedListMapper;
import wifi4eu.wifi4eu.repository.beneficiary.BeneficiaryDisplayedListRepository;
import wifi4eu.wifi4eu.repository.registration.RegistrationRepository;
import wifi4eu.wifi4eu.service.application.ApplicationService;
import wifi4eu.wifi4eu.service.security.PermissionChecker;
import wifi4eu.wifi4eu.service.user.UserConstants;
import wifi4eu.wifi4eu.service.user.UserService;
import wifi4eu.wifi4eu.utils.UserUtils;

import java.util.Date;
import java.util.Locale;

@Service("beneficiary")
public class BeneficiaryDisplayedListService {

    @Autowired
    BeneficiaryDisplayedListMapper beneficiaryDisplayedListMapper;

    @Autowired
    BeneficiaryDisplayedListRepository beneficiaryDisplayedListRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    CNSManager cnsManager;

    @Autowired
    PermissionChecker permissionChecker;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    UserUtils userUtils;

    @Autowired
    UserService userService;

    private final Logger _log = LogManager.getLogger(BeneficiaryDisplayedListService.class);

    @Transactional
    public ResponseDTO findBeneficiariesList() {
        ResponseDTO response = new ResponseDTO();
        response.setSuccess(true);
        SupplierDTO supplier;
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);

        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Retrieving beneficiary list successfully");
        try {
            supplier = permissionChecker.checkSupplierPermission();
        } catch (Exception e) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - You have no permissions " +
                    "to access");
            return permissionChecker.getAccessDeniedResponse();
        }

        response.setData(beneficiaryDisplayedListMapper.toDTOList(Lists.newArrayList(beneficiaryDisplayedListRepository.findBeneficiariesList(supplier.getUserId()))));
        _log.info("ECAS Username: " + userConnected.getEcasUsername() + " - Retrieved beneficiary list successfully");
        return response;
    }

    @Transactional
    public ResponseDTO confirmWifiIndicatorByMunicipalityId(int id) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        ResponseDTO response = new ResponseDTO();
        SupplierDTO supplier = permissionChecker.checkSupplierPermission();
        Registration registration = registrationRepository.findByMunicipalityIdAndStatus(id, 2);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Confirming report");
        if (registration != null) {

            if (!checkPermissions(registration)) {
                _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - You have no permissions " +
                        "to access");
                return permissionChecker.getAccessDeniedResponse();
            }
            // registration.setWifiIndicator(true);
            // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            registration.setInstallationSiteSubmission(new java.sql.Date(new Date().getTime()));
            registrationRepository.save(registration);
            String email = registration.getUser().getEmail();
            String name = registration.getUser().getName();
            response.setSuccess(true);
            response.setData(beneficiaryDisplayedListMapper.toDTO(beneficiaryDisplayedListRepository.findBeneficiaryByRegistrationId(registration.getId())));
            Locale locale = new Locale(UserConstants.DEFAULT_LANG);
            String lang = userUtils.getUserLangByUserId(supplier.getUserId());
            if (lang != null) {
                locale = new Locale(lang);
            }
            cnsManager.sendInstallationConfirmationNotification(email, name, locale);
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + " - Successfully confirmed report");
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + " - Confirm installation report email sent");
        } else {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - Beneficiary not found.");
            response.setSuccess(false);
            response.setData("Error querying municipality - registration");
            response.setError(new ErrorDTO(404, "error.404.beneficiaryNotFound"));
        }
        return response;
    }

    public boolean checkPermissions(Registration registration) throws AccessDeniedException {
        try {
            //first we check if user logged in is a supplier
            SupplierDTO supplier = permissionChecker.checkSupplierPermission();
            //and then we check that it has a relation to this installation site's municipality
            if (registration == null || applicationService.getApplicationBySupplierIdAndRegistrationId(supplier.getId
                    (), registration.getId()) == null) {
                throw new AccessDeniedException("403 FORBIDDEN");
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
