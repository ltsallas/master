package wifi4eu.wifi4eu.service.application;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wifi4eu.wifi4eu.common.dto.model.*;
import wifi4eu.wifi4eu.common.ecas.UserHolder;
import wifi4eu.wifi4eu.common.enums.ApplicationStatus;
import wifi4eu.wifi4eu.common.exception.AppException;
import wifi4eu.wifi4eu.common.security.UserContext;
import wifi4eu.wifi4eu.common.utils.RequestIpRetriever;
import wifi4eu.wifi4eu.entity.application.ApplicationIssueUtil;
import wifi4eu.wifi4eu.entity.logEmails.LogEmail;
import wifi4eu.wifi4eu.entity.registration.Registration;
import wifi4eu.wifi4eu.mapper.application.ApplicantListItemMapper;
import wifi4eu.wifi4eu.mapper.application.ApplicationInvalidateReasonMapper;
import wifi4eu.wifi4eu.mapper.application.ApplicationMapper;
import wifi4eu.wifi4eu.mapper.application.CorrectionRequestEmailMapper;
import wifi4eu.wifi4eu.mapper.user.UserMapper;
import wifi4eu.wifi4eu.repository.application.*;
import wifi4eu.wifi4eu.repository.logEmails.LogEmailRepository;
import wifi4eu.wifi4eu.repository.registration.RegistrationUsersRepository;
import wifi4eu.wifi4eu.repository.user.UserRepository;
import wifi4eu.wifi4eu.repository.logEmails.LogEmailRepository;
import wifi4eu.wifi4eu.repository.registration.RegistrationRepository;
import wifi4eu.wifi4eu.repository.warning.RegistrationWarningRepository;
import wifi4eu.wifi4eu.service.beneficiary.BeneficiaryService;
import wifi4eu.wifi4eu.service.call.CallService;
import wifi4eu.wifi4eu.service.municipality.MunicipalityService;
import wifi4eu.wifi4eu.service.registration.RegistrationService;
import wifi4eu.wifi4eu.service.registration.legal_files.LegalFilesService;
import wifi4eu.wifi4eu.service.user.UserConstants;
import wifi4eu.wifi4eu.service.user.UserService;
import wifi4eu.wifi4eu.service.voucher.VoucherService;
import wifi4eu.wifi4eu.util.ExcelExportGenerator;
import wifi4eu.wifi4eu.util.MailService;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.DateTimeException;
import java.util.*;

@Service
public class ApplicationService {
    @Value("${mail.server.location}")
    private String baseUrl;

    @Autowired
    ApplicantListItemMapper applicantListItemMapper;

    @Autowired
    ApplicantListItemRepository applicantListItemRepository;

    @Autowired
    ApplicationMapper applicationMapper;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    MailService mailService;

    @Autowired
    UserService userService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    MunicipalityService municipalityService;

    @Autowired
    CallService callService;

    @Autowired
    RegistrationWarningRepository registrationWarningRepository;

    @Autowired
    CorrectionRequestEmailMapper correctionRequestEmailMapper;

    @Autowired
    CorrectionRequestEmailRepository correctionRequestEmailRepository;

    @Autowired
    VoucherService voucherService;

    private static final Logger _log = LogManager.getLogger(ApplicationService.class);

    @Autowired
    BeneficiaryService beneficiaryService;

    @Autowired
    ApplicationIssueUtilRepository applicationIssueUtilRepository;

    @Autowired
    RegistrationUsersRepository registrationUsersRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    LegalFilesService legalFilesService;

    @Autowired
    ApplicationInvalidateReasonMapper applicationInvalidateReasonMapper;

    @Autowired
    ApplicationInvalidateReasonRepository applicationInvalidateReasonRepository;

    @Autowired
    LogEmailRepository logEmailRepository;

    @Deprecated
    public List<ApplicationDTO> getAllApplications() {
        return applicationMapper.toDTOList(Lists.newArrayList(applicationRepository.findAll()));
    }

    public ApplicationDTO getApplicationById(int applicationId) {
        return applicationMapper.toDTO(applicationRepository.findOne(applicationId));
    }

    /**
     * Service to register the applications coming from the Queue
     */
    public ApplicationDTO registerApplication(int callId, int userId, int registrationId,
                                              long uploadDocTimestamp, long queueTimestamp, HttpServletRequest request) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Registering application");
        CallDTO callDTO = callService.getCallById(callId);
        UserDTO userDTO = userService.getUserById(userId);
        RegistrationDTO registrationDTO = registrationService.getRegistrationById(registrationId);
        // check all the information provided exists on DB
        if (callDTO != null && userDTO != null && registrationDTO != null) {
            _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - All information provided exists");
            // check the queue date is between start/end of the call
            if (queueTimestamp / 1000000000 > callDTO.getStartDate() && queueTimestamp / 1000000000 < callDTO.getEndDate()) {
                _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - The queue is from the specified call");
                //check information on the queue is right
                if (registrationDTO.getUploadTime() == uploadDocTimestamp && registrationUsersRepository.findByUserIdAndRegistrationId(userId, registrationDTO.getId()) != null) {
                    _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - All the information of this queue is right");
                    //check if this application was received previously
                    ApplicationDTO applicationDTO = applicationMapper.toDTO(applicationRepository.findByCallIdAndRegistrationId(callId, registrationId));
                    if (applicationDTO == null || applicationDTO.getDate() > queueTimestamp) {
                        //create the application
                        if (applicationDTO == null) {
                            applicationDTO = new ApplicationDTO();
                            applicationDTO.setRegistrationId(registrationDTO.getId());
                            applicationDTO.setCallId(callDTO.getId());
                            _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - New application created");
                        }
                        applicationDTO.setDate(queueTimestamp);
                        applicationDTO = applicationMapper.toDTO(applicationRepository.save(applicationMapper.toEntity(applicationDTO)));
                        _log.log(Level.getLevel("BUSINESS"), "[ " + RequestIpRetriever.getIp(request) + " ] - ECAS Username: " + userConnected.getEcasUsername() + " - Application " + applicationDTO.getId() + " created successfully");
                        return applicationDTO;
                    } else {
                        _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - Trying to register an application existent on the DB, callId: "
                                + callId + " userId: " + userId + " registrationId: " + registrationId +
                                " uploadDocTimestamp" + uploadDocTimestamp + "queueTimestamp" + queueTimestamp);
                        return applicationDTO;
                    }
                } else {
                    _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - Trying to register an application with incorrect uploadDocTimestamp or userId not match, callId: "
                            + callId + " userId: " + userId + " registrationId: " + registrationId +
                            " uploadDocTimestamp" + uploadDocTimestamp + "queueTimestamp" + queueTimestamp);
                }
            } else {
                _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - Trying to register an application out of the call period, callId: "
                        + callId + " userId: " + userId + " registrationId: " + registrationId +
                        " uploadDocTimestamp" + uploadDocTimestamp + "queueTimestamp" + queueTimestamp);
            }
        } else {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - The information provided is wrong, callId: "
                    + callId + " userId: " + userId + " registrationId: " + registrationId +
                    " uploadDocTimestamp" + uploadDocTimestamp + "queueTimestamp" + queueTimestamp);
        }
        return null;
    }

    @Transactional
    @Deprecated
    public ApplicationDTO createApplication(ApplicationDTO applicationDTO, HttpServletRequest request) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Creating application");
        CallDTO actualCall = callService.getCallById(applicationDTO.getCallId());
        long startCallDate = actualCall.getStartDate();
        long actualDateTime = (new Date()).getTime();
        if (startCallDate > actualDateTime) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - The call is not available at the moment");
            throw new DateTimeException("The call is not available at the moment");
        }
        RegistrationDTO registration = registrationService.getRegistrationById(applicationDTO.getRegistrationId());
        UserDTO user = null;
        MunicipalityDTO municipality = null;
        if (registration.getAllFilesFlag() == 1) {
            if (registration != null) {
                user = userMapper.toDTO(userRepository.findMainUserFromRegistration(registration.getId()));
                municipality = municipalityService.getMunicipalityById(registration.getMunicipalityId());
            }
            if (user != null && municipality != null) {
                Locale locale = new Locale(UserConstants.DEFAULT_LANG);
                if (user.getLang() != null) {
                    locale = new Locale(user.getLang());
                } else {
                    _log.warn("ECAS Username: " + userConnected.getEcasUsername() + " - The user " + user.getEcasUsername() + " has not specified a language");
                }
                ResourceBundle bundle = ResourceBundle.getBundle("MailBundle", locale);
                String subject = bundle.getString("mail.voucherApply.subject");
                String msgBody = bundle.getString("mail.voucherApply.body");
                msgBody = MessageFormat.format(msgBody, municipality.getName());
                if (!userService.isLocalHost()) {
                    mailService.sendEmail(user.getEcasEmail(), MailService.FROM_ADDRESS, subject, msgBody, registration.getMunicipalityId(), "createApplication");
                    _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Email sent to" + user.getEcasEmail());
                }
            }
            ApplicationDTO application = applicationMapper.toDTO(applicationRepository.save(applicationMapper.toEntity(applicationDTO)));
            _log.log(Level.getLevel("BUSINESS"), "[ " + RequestIpRetriever.getIp(request) + " ] - ECAS Username: " + userConnected.getEcasUsername() + " - Application created");
            return application;
        }
        return null;
    }

    @Transactional
    @Deprecated
    public ApplicationDTO deleteApplication(int applicationId, HttpServletRequest request) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Removing application");
        ApplicationDTO applicationDTO = applicationMapper.toDTO(applicationRepository.findOne(applicationId));
        if (applicationDTO != null) {
            applicationRepository.delete(applicationMapper.toEntity(applicationDTO));
            return applicationDTO;
        } else {
            return null;
        }
    }

    public List<ApplicationDTO> getApplicationsBySupplierId(int supplierId) {
        return applicationMapper.toDTOList(Lists.newArrayList(applicationRepository.findBySupplierId(supplierId)));
    }

    public ApplicationDTO getApplicationByCallIdAndRegistrationId(int callId, int registrationId) {
        return applicationMapper.toDTO(applicationRepository.findByCallIdAndRegistrationId(callId, registrationId));
    }

    public List<ApplicationDTO> getApplicationsByRegistrationId(int registrationId) {
        return applicationMapper.toDTOList(Lists.newArrayList(applicationRepository.findByRegistrationId(registrationId)));
    }

    public ApplicationDTO getApplicationByRegistrationId(int callId, int registrationsId) {
        return applicationMapper.toDTO(applicationRepository.findByCallIdAndRegistrationId(callId, registrationsId));
    }

    public List<ApplicationVoucherInfoDTO> getApplicationsVoucherInfoByCall(int callId) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Retrieving applications' voucher information");
        List<ApplicationVoucherInfoDTO> applicationsVoucherInfo = new ArrayList<>();
        List<ApplicationDTO> applications = applicationMapper.toDTOList(Lists.newArrayList(applicationRepository.findByCallIdOrderByDateAsc(callId)));
        for (final ApplicationDTO appDTO : applications) {
            RegistrationDTO regDTO = registrationService.getRegistrationById(appDTO.getRegistrationId());
            if (regDTO != null) {
                final MunicipalityDTO munDTO = municipalityService.getMunicipalityById(regDTO.getMunicipalityId());
                if (munDTO != null) {
                    ApplicationVoucherInfoDTO appVoucherInfoItem = new ApplicationVoucherInfoDTO(callId, appDTO.getId(), munDTO.getName(), munDTO.getCountry(), appDTO.isVoucherAwarded());
                    applicationsVoucherInfo.add(appVoucherInfoItem);
                }
            }
        }
        return applicationsVoucherInfo;
    }

    public ApplicationVoucherInfoDTO getApplicationsVoucherInfoByApplication(int applicationId) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Retrieving applications' voucher information");
        ApplicationVoucherInfoDTO appVoucherInfoDTO = null;
        ApplicationDTO appDTO = getApplicationById(applicationId);
        if (appDTO != null) {
            RegistrationDTO regDTO = registrationService.getRegistrationById(appDTO.getRegistrationId());
            if (regDTO != null) {
                MunicipalityDTO munDTO = municipalityService.getMunicipalityById(regDTO.getMunicipalityId());
                if (munDTO != null) {
                    appVoucherInfoDTO = new ApplicationVoucherInfoDTO(appDTO.getCallId(), applicationId, munDTO.getName(), munDTO.getCountry(), appDTO.isVoucherAwarded());
                }
            }
        }
        return appVoucherInfoDTO;
    }

    public List<ApplicationDTO> getApplicationsByRegistrationNotInvalidated(int callId) {
        return applicationMapper.toDTOList(applicationRepository.findApplicationsByRegistrationNotInvalidated(callId));
    }

    public Integer countApplicationsNotInvalidated(int callId) {
        return applicationRepository.findApplicationsNotInvalidated(callId);
    }

    public List<ApplicationDTO> getApplicationsByCallFiFoOrder(int callId) {
        return applicationMapper.toDTOList(applicationRepository.findByCallIdOrderByDateAsc(callId));
    }

    public List<ApplicationDTO> findByCallIdOrderByDateBeforeCallDateAsc(int callId, long startDate) {
        return applicationMapper.toDTOList(applicationRepository.findByCallIdOrderByDateBAsc(callId, startDate));
    }

    public List<ApplicationDTO> getApplicationByCallAndCountry(int callId, String country, long date) {
        return applicationMapper.toDTOList(applicationRepository.findApplicationsByCountry(callId, country, date));
    }

    public List<ApplicationDTO> getApplicationsCountryNameCall(int callId, String country) {
        return applicationMapper.toDTOList(applicationRepository.findApplicationsCountry(country, callId));
    }

    public List<Integer> getApplicationsIdByCountryAndNameAndCall(int callId, String country, long date) {
        return applicationRepository.findIdApplications(country, callId, date);
    }

    public Integer countApplicationWithSameMunicipalityName(int lauId, int callId, long date) {
        return applicationRepository.countApplicationsBySameMunicipality(lauId, callId, date);
    }

    public List<ApplicantListItemDTO> findDgconnApplicantsList(Integer callId, String country, String name, PagingSortingDTO pagingSortingData) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Retrieving applicants");
        List<ApplicantListItemDTO> applicantsList;
        if (country == null) {
            country = "%";
        }
        switch (pagingSortingData.getOrderField()) {
            case "name":
                if (pagingSortingData.getOrderType() == -1) {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByNameDesc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByNameDesc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                } else {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByNameAsc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByNameAsc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                }
                break;
            case "countryCode":
                if (pagingSortingData.getOrderType() == -1) {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByCountryCodeDesc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByCountryCodeDesc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                } else {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByCountryCodeAsc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByCountryCodeAsc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                }
                break;
            case "counter":
                if (pagingSortingData.getOrderType() == -1) {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByCounterDesc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByCounterDesc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                } else {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByCounterAsc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByCounterAsc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                }
                break;
            case "mediation":
                if (pagingSortingData.getOrderType() == -1) {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByMediationDesc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByMediationDesc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                } else {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByMediationAsc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByMediationAsc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                }
                break;
            case "supportingdocuments":
                if (pagingSortingData.getOrderType() == -1) {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderBySupportingDocumentsDesc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderBySupportingDocumentsDesc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                } else {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderBySupportingDocumentsAsc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderBySupportingDocumentsAsc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                }
                break;
            default:
                if (pagingSortingData.getOrderType() == -1) {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByLauIdDesc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByLauIdDesc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                } else {
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListContainingNameOrderByLauIdAsc(callId, country, name, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                            break;
                        }
                    }
                    applicantsList = applicantListItemMapper.toDTOList(applicantListItemRepository.findDgconnApplicantsListOrderByLauIdAsc(callId, country, pagingSortingData.getOffset(), pagingSortingData.getCount()));
                }
                break;
        }

        //setIssues(applicantsList);

        return applicantsList;
    }

    private void setIssues(List<ApplicantListItemDTO> applicantsList) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Setting issues");
        for (ApplicantListItemDTO applicantListItemDTO : applicantsList) {
            List<Integer> warnings = registrationWarningRepository.findAllByLauId(applicantListItemDTO.getLauId());
            applicantListItemDTO.setIssueStatus(warnings);
        }
    }

    public ApplicationDTO sendLegalDocumentsCorrection(ApplicationDTO application, HttpServletRequest request) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Sending legal documents for correction");
        ApplicationDTO applicationDB = applicationMapper.toDTO(applicationRepository.findOne(application.getId()));
        List<LegalFileCorrectionReasonDTO> legalFiles = registrationService.getLegalFilesByRegistrationId(applicationDB.getRegistrationId());
        boolean pendingFollowup = false;
        for (LegalFileCorrectionReasonDTO legalFile : legalFiles) {
            if (legalFile.getRequestCorrection() && legalFile.getCorrectionReason() != null) {
                pendingFollowup = true;
                _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Legal file: " + legalFile.getCorrectionReason() + " from registration " + applicationDB.getRegistrationId() + " is pending for correction");
                break;
            }
        }
        RegistrationDTO registration = registrationService.getRegistrationById(applicationDB.getRegistrationId());
        if (pendingFollowup) {
            registration.setAllFilesFlag(0);
            applicationDB.setStatus(ApplicationStatus.PENDING_FOLLOWUP.getValue());
        } else {
            registration.setAllFilesFlag(1);
            applicationDB.setStatus(ApplicationStatus.HOLD.getValue());
        }
        applicationDB.setInvalidateReason(null);
        registrationService.saveRegistration(registration);
        ApplicationDTO applicationResponse = applicationMapper.toDTO(applicationRepository.save(applicationMapper.toEntity(applicationDB)));
        _log.log(Level.getLevel("BUSINESS"), "[ " + RequestIpRetriever.getIp(request) + " ] - ECAS Username: " + userConnected.getEcasUsername() + " - Legal files from the application are sent for correction");
        return applicationResponse;
    }

    public List<ApplicationDTO> getApplicationsByCallId(int callId) {
        return applicationMapper.toDTOList(Lists.newArrayList(applicationRepository.findByCallIdOrderByDateAsc(callId)));
    }

    public List<ApplicationDTO> getApplicationsByCallIdAndLauId(int callId, int lauId) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Retrieving applications");
        List<ApplicationDTO> applications = new ArrayList<>();
        for (ApplicationDTO application : getApplicationsByCallIdAndLauIdCustom(callId, lauId)) {
            RegistrationDTO registration = registrationService.getRegistrationById(application.getRegistrationId());
            if (registration != null) {
                MunicipalityDTO municipality = municipalityService.getMunicipalityById(registration.getMunicipalityId());
                if (municipality != null) {
                    if (municipality.getLauId() == lauId) {
                        applications.add(application);
                        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Application with id " + application.getId() + " added to the list");
                    }
                }
            }
        }
        return applications;
    }

    //Optimal
    private List<ApplicationDTO> getApplicationsByCallIdAndLauIdCustom(int callId, int lauId) {
        return applicationMapper.toDTOList(Lists.newArrayList(applicationRepository.findByCallIdAndLauIdOrderByDateAsc(callId, lauId)));
    }

    public byte[] exportExcelDGConnApplicantsList(Integer callId, String country) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Exporting excel");
        int totalCount = municipalityService.getCountDistinctMunicipalitiesThatAppliedCall(callId, country);
        int pageSize = totalCount;
        PagingSortingDTO pagingSortingData = new PagingSortingDTO(0, pageSize, "lauId", 1);
        List<ApplicantListItemDTO> applicants = findDgconnApplicantsList(callId, country, null, pagingSortingData);
        ExcelExportGenerator excelExportGenerator = new ExcelExportGenerator(applicants, ApplicantListItemDTO.class);
        _log.info("ECAS Username: " + userConnected.getEcasUsername() + " - Excel exported");
        return excelExportGenerator.exportExcelFile("applicants").toByteArray();
    }

    public byte[] exportExcelDGConnApplicantsListContainingName(Integer callId, String country, String name) {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Exporting excel");
        int totalCount = municipalityService.getCountDistinctMunicipalitiesThatAppliedCallContainingName(callId, country, name);
        int pageSize = totalCount;
        PagingSortingDTO pagingSortingData = new PagingSortingDTO(0, pageSize, "lauId", 1);
        List<ApplicantListItemDTO> applicants = findDgconnApplicantsList(callId, country, name, pagingSortingData);
        ExcelExportGenerator excelExportGenerator = new ExcelExportGenerator(applicants, ApplicantListItemDTO.class);
        _log.info("ECAS Username: " + userConnected.getEcasUsername() + " - Excel exported");
        return excelExportGenerator.exportExcelFile("applicants").toByteArray();
    }

    public CorrectionRequestEmailDTO sendCorrectionEmails(Integer callId) throws Exception {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        if (!checkIfCorrectionRequestEmailIsAvailable(callId)) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "-Emails can only be sent if call is closed and has " + "correction " +
                    "requests, for the call:  " + "" + callId);
            throw new AppException("Emails can only be sent if call is closed and has " + "correction requests, for the call:  " + "" + callId);
        }
        List<ApplicationIssueUtil> applications = applicationIssueUtilRepository.findApplicationIssueUtilByCallAndStatus(callId, ApplicationStatus.PENDING_FOLLOWUP.getValue());
        CorrectionRequestEmailDTO correctionRequest = null;
        CorrectionRequestEmailDTO lastCorrectionRequestEmail = getLastCorrectionRequestEmailInCall(callId);
        Integer buttonPressedCounter;
        if (lastCorrectionRequestEmail != null) {
            buttonPressedCounter = lastCorrectionRequestEmail.getButtonPressedCounter() + 1;
        } else {
            buttonPressedCounter = 1;
        }
        if (!applications.isEmpty()) {
            for (ApplicationIssueUtil application : applications) {
                Locale locale = new Locale(UserConstants.DEFAULT_LANG);
                if (application.getUserLang() != null) {
                    locale = new Locale(application.getUserLang());
                }
                ResourceBundle bundle = ResourceBundle.getBundle("MailBundle", locale);
                String subject = bundle.getString("mail.correctionRequestEmail.subject");
                String header = bundle.getString("mail.correctionRequestEmail.header");
                String msgBody = bundle.getString("mail.correctionRequestEmail.body1");
                String msgBody2 = bundle.getString("mail.correctionRequestEmail.body2");
                String signOff = bundle.getString("mail.correctionRequestEmail.signOff");
                String[] correctionReasons = new String[6];
                correctionReasons[0] = bundle.getString("mail.correctionRequestEmail.reason1");
                correctionReasons[1] = bundle.getString("mail.correctionRequestEmail.reason2");
                correctionReasons[2] = bundle.getString("mail.correctionRequestEmail.reason3");
                correctionReasons[3] = bundle.getString("mail.correctionRequestEmail.reason4");
                correctionReasons[4] = "";
                correctionReasons[5] = bundle.getString("mail.correctionRequestEmail.reason6");
                String reason5Case1 = bundle.getString("mail.correctionRequestEmail.reason5-1");
                String reason5Case2 = bundle.getString("mail.correctionRequestEmail.reason5-2");
                String reason5Case3 = bundle.getString("mail.correctionRequestEmail.reason5-3");
                String[] documentTypesBody1 = {"", ""};
                String[] documentTypesBody2 = {"", ""};
                String emailBody = "";
                Registration registration = registrationRepository.findOne(application.getRegistrationId());
                LogEmail lastEmailSent = logEmailRepository.findTopByMunicipalityIdAndActionOrderBySentDateDesc(registration.getMunicipality().getId(), "sendCorrectionEmails");
                List<LegalFileCorrectionReasonDTO> legalFilesCorrectionReasons = registrationService.getLegalFilesByRegistrationId(application.getRegistrationId());
                for (LegalFileCorrectionReasonDTO legalFileCorrectionReason : legalFilesCorrectionReasons) {
                    if (legalFileCorrectionReason.getRequestCorrection() && (lastEmailSent == null || legalFileCorrectionReason.getRequestCorrectionDate().getTime() > lastEmailSent.getSentDate())) {
                        String emailString = "";
                        switch (legalFileCorrectionReason.getType()) {
                            case 1:
                                correctionReasons[4] = reason5Case1;
                                emailString = bundle.getString("mail.correctionRequestEmail.type1");
                                documentTypesBody1[0] = MessageFormat.format(emailString, correctionReasons[legalFileCorrectionReason.getCorrectionReason()]);
                                break;
                            case 2:
                                correctionReasons[4] = reason5Case1;
                                emailString = bundle.getString("mail.correctionRequestEmail.type3");
                                documentTypesBody2[0] = MessageFormat.format(emailString, correctionReasons[legalFileCorrectionReason.getCorrectionReason()]);
                                break;
                            case 3:
                                correctionReasons[4] = reason5Case2;
                                emailString = bundle.getString("mail.correctionRequestEmail.type2");
                                documentTypesBody1[1] = MessageFormat.format(emailString, correctionReasons[legalFileCorrectionReason.getCorrectionReason()]);
                                break;
                            case 4:
                                correctionReasons[4] = reason5Case3;
                                emailString = bundle.getString("mail.correctionRequestEmail.type4");
                                documentTypesBody2[1] = MessageFormat.format(emailString, correctionReasons[legalFileCorrectionReason.getCorrectionReason()]);
                                break;
                        }
                    }
                }
                //if document is type 1 or 3 then we need to show body 1
                boolean isBody1 = !documentTypesBody1[0].isEmpty() || !documentTypesBody1[1].isEmpty();
                //if document is type 2 or 4 then we need to show body 2
                boolean isBody2 = !documentTypesBody2[0].isEmpty() || !documentTypesBody2[1].isEmpty();
                msgBody = MessageFormat.format(msgBody, documentTypesBody1);
                msgBody2 = MessageFormat.format(msgBody2, documentTypesBody2);
                if (isBody1 && isBody2) {
                    emailBody = header + msgBody + msgBody2 + signOff;
                } else if (isBody1) {
                    emailBody = header + msgBody + signOff;
                } else if (isBody2) {
                    emailBody = header + msgBody2 + signOff;
                }

                if (!emailBody.isEmpty()) {
                    mailService.sendEmail(application.getUserEcasEmail(), MailService.FROM_ADDRESS, subject, emailBody, registration
                            .getMunicipality().getId(), "sendCorrectionEmails");
                }
            }
            correctionRequest = new CorrectionRequestEmailDTO(null, callId, new Date().getTime(), buttonPressedCounter);
            correctionRequest = correctionRequestEmailMapper.toDTO(correctionRequestEmailRepository.save(correctionRequestEmailMapper.toEntity(correctionRequest)));
        }
        return correctionRequest;
    }

    public CorrectionRequestEmailDTO getLastCorrectionRequestEmailInCall(Integer callId) {
        List<CorrectionRequestEmailDTO> correctionRequests = correctionRequestEmailMapper.toDTOList(correctionRequestEmailRepository.findAllByCallIdOrderByDateDesc(callId));
        if (!correctionRequests.isEmpty()) {
            return correctionRequests.get(0);
        } else {
            return null;
        }
    }

    public boolean checkIfCorrectionRequestEmailIsAvailable(Integer callId) {
        if (callService.isCallClosed(callId)) {
            List<ApplicationDTO> pendingFollowupApps = applicationMapper.toDTOList(applicationRepository.findByCallIdAndStatus(callId,
                    ApplicationStatus.PENDING_FOLLOWUP.getValue()));
            if (!pendingFollowupApps.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public ApplicationDTO rejectApplicationVoucherAssigment(int applicationId) {
        ApplicationDTO applicationDTO = getApplicationById(applicationId);
        if (applicationDTO == null) {
            throw new AppException("Application not found with id: " + applicationId);
        }

        VoucherAssignmentAuxiliarDTO voucherAssignmentAuxiliar = voucherService.getVoucherAssignmentAuxiliarByCall
                (applicationDTO.getCallId());
        if (voucherAssignmentAuxiliar == null || !voucherAssignmentAuxiliar.getHasPreListSaved()) {
            throw new AppException("Pre List hasn't been saved for the call of the application with id: " +
                    applicationId);
        }

        applicationDTO.setRejected(true);
        return applicationMapper.toDTO(applicationRepository.save(applicationMapper.toEntity(applicationDTO)));
    }

    public ApplicationDTO selectApplicationVoucherAssigment(int applicationId) {
        ApplicationDTO applicationDTO = getApplicationById(applicationId);
        if (applicationDTO == null) {
            throw new AppException("Application not found with id: " + applicationId);
        }

        VoucherAssignmentAuxiliarDTO voucherAssignmentAuxiliar = voucherService.getVoucherAssignmentAuxiliarByCall
                (applicationDTO.getCallId());
        if (voucherAssignmentAuxiliar == null || !voucherAssignmentAuxiliar.getHasPreListSaved()) {
            throw new AppException("Pre List hasn't been saved for the call of the application with id: " +
                    applicationId);
        }

        applicationDTO.setRejected(false);
        return applicationMapper.toDTO(applicationRepository.save(applicationMapper.toEntity(applicationDTO)));
    }

    /**
     * This method is called by the cron ScheduledTasks.deadlineSubmissionForRequestDocuments().
     * It gets all applicants that are on status follow up and check if they have files on requested correction, that were sent an email on this
     * day and not afterwards. Also checks if the document was put as requested correction before the date. (This check is necessary because the user can
     * send the email as many times as its want and we need to make sure that we don't invalidate beneficiaries that still have time to upload the
     * requested documents.
     * If any of those files that are requested, are type 1 or 3 and the beneficiary hasn't uploaded them, this method invalidates this beneficiary.
     *
     * @param callId
     * @param dateRequest "deadline date". The date that is 7 days ago, its use to compare with the uploaded file and email log.
     */
    public void invalidateApplicationsPostRequestForDocumentsPastDeadline(Integer callId, long dateRequest) {
        List<ApplicationIssueUtil> applications = applicationIssueUtilRepository.findApplicationIssueUtilByCallAndStatus(callId, ApplicationStatus
                .PENDING_FOLLOWUP.getValue());
        if (!applications.isEmpty()) {
            for (ApplicationIssueUtil applicationUtil : applications) {
                if (hasThisBeneficiaryBeenSentCorrectionEmailThisDay(applicationUtil.getRegistrationId(), dateRequest)) {
                    List<LegalFileCorrectionReasonDTO> legalFilesCorrectionReasons = registrationService.getLegalFilesByRegistrationId(applicationUtil.getRegistrationId());
                    for (LegalFileCorrectionReasonDTO legalFileCorrectionReason : legalFilesCorrectionReasons) {
                        int legalType = legalFileCorrectionReason.getType();
                        //if document as requested correction and legal type is 1 or 3 we need to check the uploaded time of the file and compare it
                        //if this document in particular was requested before the the email was sent we check to invalidate it
                        if (legalFileCorrectionReason.getRequestCorrection() && (legalType == 1 || legalType == 3) && legalFileCorrectionReason
                                .getRequestCorrectionDate().getTime() < dateRequest) {
                            LegalFilesDTO legalFile = legalFilesService.getLegalFileByRegistrationIdFileType(applicationUtil.getRegistrationId(),
                                    legalType);
                            if (legalFile == null || (legalFile.getUploadTime().getTime() < dateRequest)) {
                                // the file either hasn't been uploaded again or never was. We proceed to invalidate this application.
                                ApplicationDTO applicationDTO = getApplicationById(applicationUtil.getApplicationId());
                                _log.log(Level.getLevel("BUSINESS"), "SCHEDULED TASK: Deadline for Requested Documents - INVALIDATING application with " + "" + "the id: " + applicationDTO.getId() + ". Legal file type: " + legalType);
                                ApplicationInvalidateReasonDTO invalidReasonViewDTO = new ApplicationInvalidateReasonDTO(applicationDTO.getId(), 7);
                                applicationInvalidateReasonRepository.save(applicationInvalidateReasonMapper.toEntity(invalidReasonViewDTO));
                                applicationDTO.setStatus(ApplicationStatus.KO.getValue());
                                applicationRepository.save(applicationMapper.toEntity(applicationDTO));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * This method is called by the cron ScheduledTasks.deadlineSubmissionForRequestDocuments().
     * We need to check that this registration was indeed sent an sendCorrectionEmail on this day or afterwards.
     *
     * @param registrationId
     * @param deadlineDay the day that we want to know if this registration was sent an sendCorrectionEmails
     * @return if this registration was sent an sendCorrectionEmail on deadlineDay
     */
    public boolean hasThisBeneficiaryBeenSentCorrectionEmailThisDay(Integer registrationId, long deadlineDay){
        Integer municipalityId = registrationService.getRegistrationById(registrationId).getMunicipalityId();
        List<LogEmail> logEmails = logEmailRepository.findAllByMunicipalityIdAndActionOrderBySentDateDesc(municipalityId, "sendCorrectionEmails");
        for (LogEmail logEmail : logEmails){
            //if there's a log of a sendCorrectionEmail sent on this day, this applicant's deadline is today
            //otherwise we will not invalidate them
            if((logEmail.getSentDate() - deadlineDay) / (24 * 60 * 60 * 1000) == 0){
                return true;
            }
        }
        return false;
    }

}