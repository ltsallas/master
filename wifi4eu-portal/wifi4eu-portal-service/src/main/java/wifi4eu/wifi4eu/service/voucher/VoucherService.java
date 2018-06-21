package wifi4eu.wifi4eu.service.voucher;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wifi4eu.wifi4eu.common.dto.model.*;
import wifi4eu.wifi4eu.common.dto.rest.ResponseDTO;
import wifi4eu.wifi4eu.common.ecas.UserHolder;
import wifi4eu.wifi4eu.common.enums.ApplicationStatus;
import wifi4eu.wifi4eu.common.enums.SelectionStatus;
import wifi4eu.wifi4eu.common.enums.VoucherAssignmentStatus;
import wifi4eu.wifi4eu.common.exception.AppException;
import wifi4eu.wifi4eu.common.security.UserContext;
import wifi4eu.wifi4eu.entity.voucher.VoucherAssignment;
import wifi4eu.wifi4eu.entity.voucher.VoucherSimulation;
import wifi4eu.wifi4eu.mapper.application.ApplicationMapper;
import wifi4eu.wifi4eu.mapper.voucher.VoucherAssignmentAuxiliarMapper;
import wifi4eu.wifi4eu.mapper.voucher.VoucherAssignmentMapper;
import wifi4eu.wifi4eu.mapper.voucher.VoucherSimulationMapper;
import wifi4eu.wifi4eu.repository.application.ApplicationRepository;
import wifi4eu.wifi4eu.repository.user.UserRepository;
import wifi4eu.wifi4eu.repository.voucher.VoucherAssignmentAuxiliarRepository;
import wifi4eu.wifi4eu.repository.voucher.VoucherAssignmentRepository;
import wifi4eu.wifi4eu.repository.voucher.VoucherSimulationRepository;
import wifi4eu.wifi4eu.service.application.ApplicationService;
import wifi4eu.wifi4eu.service.call.CallService;
import wifi4eu.wifi4eu.service.location.LauService;
import wifi4eu.wifi4eu.service.location.NutsService;
import wifi4eu.wifi4eu.service.municipality.MunicipalityService;
import wifi4eu.wifi4eu.service.registration.RegistrationService;
import wifi4eu.wifi4eu.service.user.UserConstants;
import wifi4eu.wifi4eu.service.user.UserService;
import wifi4eu.wifi4eu.service.warning.RegistrationWarningService;
import wifi4eu.wifi4eu.util.MailService;
import wifi4eu.wifi4eu.util.VoucherSimulationExportGenerator;

import java.text.MessageFormat;
import java.util.*;

@Service("portalVoucherService")
public class VoucherService {

    private Logger _log = LogManager.getLogger(this.getClass());

    @Autowired
    VoucherAssignmentMapper voucherAssignmentMapper;

    @Autowired
    VoucherAssignmentAuxiliarMapper voucherAssignmentAuxiliarMapper;

    @Autowired
    VoucherSimulationMapper voucherSimulationMapper;

    @Autowired
    CallService callService;

    @Autowired
    UserService userService;

    @Autowired
    VoucherManagementService voucherManagementService;

    @Autowired
    RegistrationWarningService registrationWarningService;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    VoucherSimulationRepository voucherSimulationRepository;

    @Autowired
    VoucherAssignmentRepository voucherAssignmentRepository;

    @Autowired
    VoucherAssignmentAuxiliarRepository voucherAssignmentAuxiliarRepository;

    @Autowired
    ApplicationMapper applicationMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailService mailService;

    @Autowired
    private SimpleRegistrationService simpleRegistrationService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    SimpleMunicipalityService simpleMunicipalityService;

    @Autowired
    SimpleLauService simpleLauService;

    @Autowired
    MunicipalityService municipalityService;

    @Autowired
    LauService lauService;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    NutsService nutsService;

    UserDTO userConnected;

    public List<VoucherAssignmentDTO> getAllVoucherAssignment() {
        return voucherAssignmentMapper.toDTOList(Lists.newArrayList(voucherAssignmentRepository.findAll()));
    }

    public VoucherAssignmentDTO getVoucherAssignmentById(int voucherAssignmentId) {
        return voucherAssignmentMapper.toDTO(voucherAssignmentRepository.findOne(voucherAssignmentId));
    }

    public VoucherAssignmentDTO getVoucherAssignmentByCall(int callId) {
        return voucherAssignmentMapper.toDTO(voucherAssignmentRepository.findByCallId(callId));
    }

    public VoucherAssignmentAuxiliarDTO getVoucherAssignmentByCallAndStatus(int callId, int status) {
        return voucherAssignmentAuxiliarMapper.toDTO(voucherAssignmentAuxiliarRepository.findByCallIdAndStatusAux(callId, status));
    }

    public VoucherAssignmentAuxiliarDTO getVoucherAssignmentAuxiliarByCall(int callId) {
        VoucherAssignmentAuxiliarDTO voucherAssignmentAuxiliarDTO = voucherAssignmentAuxiliarMapper.toDTO(voucherAssignmentAuxiliarRepository.findByCallIdAndStatusAux(callId, 1));
        if (voucherAssignmentAuxiliarDTO == null) {
            throw new AppException("Voucher assigment not found for call id: " + callId);
        }

        VoucherAssignmentAuxiliarDTO voucherAssignmentPreList = voucherAssignmentAuxiliarMapper.toDTO(voucherAssignmentAuxiliarRepository.findByCallIdAndStatusAux(callId, 2));

        voucherAssignmentAuxiliarDTO.setHasPreListSaved(voucherAssignmentPreList != null);
        if (voucherAssignmentPreList != null) {
            voucherAssignmentAuxiliarDTO.setPreListExecutionDate(voucherAssignmentPreList.getExecutionDate());
        }

        VoucherAssignmentAuxiliarDTO voucherAssignmentFreezeList = voucherAssignmentAuxiliarMapper.toDTO(voucherAssignmentAuxiliarRepository.findByCallIdAndStatusAux(callId, 3));
        voucherAssignmentAuxiliarDTO.setHasFreezeListSaved(voucherAssignmentFreezeList != null);
        if (voucherAssignmentFreezeList != null) {
            voucherAssignmentAuxiliarDTO.setFreezeLisExecutionDate(voucherAssignmentFreezeList.getExecutionDate());
            voucherAssignmentAuxiliarDTO.setNotifiedDate(voucherAssignmentFreezeList.getNotifiedDate());
        }

        return voucherAssignmentAuxiliarDTO;
    }

    public ResponseDTO getVoucherSimulationByVoucherAssignment(int voucherAssignmentId, String country, String municipality, Pageable pageable) {
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Retrieving voucher simulations - voucherAssignment: " + voucherAssignmentId + "municipality: " + municipality + ", country: " + country);
        Page<VoucherSimulation> simulationPaged = null;
        List<VoucherSimulationDTO> listSimulation = new ArrayList<>();
        int totalElements = 0;
        String sortDirection = "ASC";
        if (pageable.getSort().getOrderFor("municipalityName") != null) {
            sortDirection = pageable.getSort().getOrderFor("municipalityName").getDirection().name();
        }

        if (country.isEmpty() || country.equalsIgnoreCase("All")) {
            country = "";
        }
        if (municipality.isEmpty() || municipality.equalsIgnoreCase("All")) {
            municipality = "";
        }
        if (pageable.getSort().getOrderFor("municipalityName") != null) {
            listSimulation = voucherSimulationMapper.toDTOList(voucherSimulationRepository.findAllByVoucherAssignmentAndMunicipalityInCountryOrderedByMunicipalityName(voucherAssignmentId, municipality, country, pageable.getOffset(), pageable.getPageSize(), sortDirection));
            totalElements = voucherSimulationRepository.countAllByVoucherAssignmentAndMunicipalityInCountryOrderedByMunicipalityName(voucherAssignmentId, municipality, country);
        } else {

            simulationPaged = voucherSimulationRepository.findAllByVoucherAssignmentAndMunicipalityInCountryOrderedByEuRank(voucherAssignmentId, country, municipality, pageable);
            listSimulation = voucherSimulationMapper.toDTOList(Lists.newArrayList(simulationPaged.getContent()));
        }
        for (VoucherSimulationDTO voucherSimulation : listSimulation) {
            List<RegistrationWarningDTO> warningList = registrationWarningService.getWarningsByRegistrationId(voucherSimulation.getApplication().getRegistrationId());
            SimpleMunicipalityDTO simpleMunicipalityDTO = simpleMunicipalityService.getMunicipalityById(voucherSimulation.getMunicipality());
            voucherSimulation.setMunicipalityName(simpleMunicipalityDTO.getName());
            voucherSimulation.setLau(simpleMunicipalityDTO.getLau());
            voucherSimulation.setRegistrationWarningDTO(warningList);
        }
        if (pageable.getSort().getOrderFor("municipalityName") != null) {
            return new ResponseDTO(true, listSimulation, (long) totalElements, null);
        }
        return new ResponseDTO(true, listSimulation, simulationPaged.getTotalElements(), null);
    }

    public byte[] exportVoucherSimulation(int voucherAssignmentId, String country, String municipalityName, Pageable pageable) {
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Downloading excel voucher simulation with parameters - voucherAssignment: " + voucherAssignmentId + "municipality: " + municipalityName + ", country: " + country);
        List<VoucherSimulationDTO> simulationDTOS = (List<VoucherSimulationDTO>) getVoucherSimulationByVoucherAssignment(voucherAssignmentId, country, municipalityName, pageable).getData();

        VoucherSimulationExportGenerator excelExportGenerator = new VoucherSimulationExportGenerator(simulationDTOS, VoucherSimulationDTO.class);
        return excelExportGenerator.exportExcelFile("voucher_simulation").toByteArray();
    }

    public List<VoucherSimulationDTO> getVoucherSimulationsByVoucherAssigmentId(int voucherAssignmentId) {
        return voucherSimulationMapper.toDTOList(voucherSimulationRepository.findAllByVoucherAssignmentOrderByEuRank(voucherAssignmentId));
    }

    public boolean checkSavePreSelectionEnabled(int voucherAssignmentId) {

        List<VoucherSimulationDTO> simulationList = getVoucherSimulationsByVoucherAssigmentId(voucherAssignmentId);

        for (VoucherSimulationDTO simulation : simulationList) {
            if (simulation.getNumApplications() > 1) {
                return false;
            }
            if (simulation.getApplication().getStatus() != ApplicationStatus.OK.getValue()) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public VoucherAssignmentDTO saveFreezeListSimulation(int voucherAssignmentId, int callId) {

        CallDTO callDTO = callService.getCallById(callId);

        if (callDTO == null) {
            throw new AppException("Call not exists");
        }

        VoucherAssignmentAuxiliarDTO voucherAssignmentAuxiliarDTO = voucherAssignmentAuxiliarMapper.toDTO(voucherAssignmentAuxiliarRepository.findByCallIdAndStatusAux(callDTO.getId(), 3));

        if (voucherAssignmentAuxiliarDTO != null) {
            throw new AppException("VoucherAssignment with id " + voucherAssignmentId + " not found");
        }

        List<VoucherSimulationDTO> simulationDTOS = voucherSimulationMapper.toDTOList(voucherSimulationRepository.findAllByVoucherAssignmentAndStatusOrderByEuRank(voucherAssignmentId));
        Set<VoucherSimulationDTO> simulationDTOSet = new HashSet<>();

        VoucherAssignmentDTO freezeVoucherAssignment = new VoucherAssignmentDTO();
        freezeVoucherAssignment.setStatus(VoucherAssignmentStatus.FREEZE_LIST.getValue());
        freezeVoucherAssignment.setExecutionDate(new Date().getTime());
        freezeVoucherAssignment.setUser(userService.getUserByUserContext(UserHolder.getUser()));
        freezeVoucherAssignment.setCall(callDTO);

        VoucherAssignmentDTO result = voucherAssignmentMapper.toDTO(voucherAssignmentRepository.save(voucherAssignmentMapper.toEntity(freezeVoucherAssignment)));

        for (VoucherSimulationDTO voucherSimulation : simulationDTOS) {
            VoucherSimulationDTO voucherSimulationDTO = new VoucherSimulationDTO();
            voucherSimulationDTO.setApplication(voucherSimulation.getApplication());
            voucherSimulationDTO.setCountry(voucherSimulation.getCountry());
            voucherSimulationDTO.setCountryRank(voucherSimulation.getCountryRank());
            voucherSimulationDTO.setEuRank(voucherSimulation.getEuRank());
            voucherSimulationDTO.setLau(voucherSimulation.getLau());
            voucherSimulationDTO.setMunicipality(voucherSimulation.getMunicipality());
            voucherSimulationDTO.setMunicipalityName(voucherSimulation.getMunicipalityName());
            voucherSimulationDTO.setNumApplications(voucherSimulation.getNumApplications());
            voucherSimulationDTO.setSelectionStatus(voucherSimulation.getSelectionStatus());
            if (voucherSimulation.getSelectionStatus() == SelectionStatus.MAIN_LIST.getValue()) {
                voucherSimulationDTO.setSelectionStatus(SelectionStatus.SELECTED.getValue());
            }
            voucherSimulationDTO.setRejected(voucherSimulation.getRejected());
            voucherSimulationDTO.setVoucherAssignment(result.getId());
            simulationDTOSet.add(voucherSimulationDTO);
        }

        result.setVoucherSimulations(simulationDTOSet);
        return voucherAssignmentMapper.toDTO(voucherAssignmentRepository.save(voucherAssignmentMapper.toEntity(result)));
    }

    @Transactional
    public VoucherAssignmentDTO savePreListSimulation(int voucherAssignmentId, int callId) {
        CallDTO callDTO = callService.getCallById(callId);

        if (callDTO == null) {
            throw new AppException("Call not exists");
        }

        if (checkSavePreSelectionEnabled(voucherAssignmentId)) {

            VoucherAssignmentAuxiliarDTO auxiliarDTO = voucherAssignmentAuxiliarMapper.toDTO(voucherAssignmentAuxiliarRepository.findByCallIdAndStatusAux(callId, 1));
            if (auxiliarDTO == null) {
                throw new AppException("Voucher assigment not found");
            }
            List<VoucherSimulationDTO> simulationList = getVoucherSimulationsByVoucherAssigmentId(auxiliarDTO.getId());

            VoucherAssignmentDTO voucherAssignment = voucherAssignmentMapper.toDTO(voucherAssignmentRepository.findByCallIdAndStatusEquals(callId, 2));

            if (voucherAssignment != null) {
                //voucherAssignmentRepository.delete(voucherAssignmentMapper.toEntity(voucherAssignment));
                throw new AppException("Existing Pre-selection list for callId: " + callId);
            }

            voucherAssignment = new VoucherAssignmentDTO();
            voucherAssignment.setCall(callService.getCallById(callId));
            voucherAssignment.setUser(userService.getUserByUserContext(UserHolder.getUser()));
            voucherAssignment.setExecutionDate(new Date().getTime());

            voucherAssignment.setStatus(VoucherAssignmentStatus.PRE_LIST.getValue());

            Set<VoucherSimulationDTO> simulationsPreSave = new HashSet<>();

            VoucherAssignmentDTO result = voucherAssignmentMapper.toDTO(voucherAssignmentRepository.save(voucherAssignmentMapper.toEntity(voucherAssignment)));

            for (VoucherSimulationDTO voucherSimulation : simulationList) {
                VoucherSimulationDTO voucherSimulationDTO = new VoucherSimulationDTO();
                voucherSimulationDTO.setApplication(voucherSimulation.getApplication());
                voucherSimulationDTO.setCountry(voucherSimulation.getCountry());
                voucherSimulationDTO.setCountryRank(voucherSimulation.getCountryRank());
                voucherSimulationDTO.setEuRank(voucherSimulation.getEuRank());
                voucherSimulationDTO.setLau(voucherSimulation.getLau());
                voucherSimulationDTO.setMunicipality(voucherSimulation.getMunicipality());
                voucherSimulationDTO.setMunicipalityName(voucherSimulation.getMunicipalityName());
                voucherSimulationDTO.setNumApplications(voucherSimulation.getNumApplications());
                voucherSimulationDTO.setSelectionStatus(voucherSimulation.getSelectionStatus());
                voucherSimulationDTO.setRejected(voucherSimulation.getRejected());
                voucherSimulationDTO.setVoucherAssignment(result.getId());
                simulationsPreSave.add(voucherSimulationDTO);
            }

            result.setVoucherSimulations(simulationsPreSave);

            result = voucherAssignmentMapper.toDTO(voucherAssignmentRepository.save(voucherAssignmentMapper.toEntity(result)));

            voucherSimulationRepository.updateApplicationsInVoucherSimulationByVoucherAssignment(1, result.getId());
            return result;
        } else {
            throw new AppException("Error saving pre-selected list");
        }
    }

    @Transactional
    public ResponseDTO simulateVoucherFast(int callId) {
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Executing voucher simulation for call: " + callId);
        HashMap<Integer, SimpleMunicipalityDTO> municipalityHashMap = new HashMap<>();
        HashMap<Integer, SimpleLauDTO> lauHashMap = new HashMap<>();
        HashMap<Integer, SimpleRegistrationDTO> registrationsHashMap = new HashMap<>();

        UserContext userContext = UserHolder.getUser();

        if (userContext != null) {
            CallDTO call = callService.getCallById(callId);
            if (call == null) {
                _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - Call not exist");
                return new ResponseDTO(false, "Call not exist", null);
            }

            if (call.getNumberVouchers() == 0) {
                _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - Simulation stopped due to number of vouchers to be assigned");
                return new ResponseDTO(false, "Simulation stopped", null);
            }

            UserDTO userDTO = userService.getUserByUserContext(userContext);

            int callNr = call.getId();
            int vouchersToBeAssigned = call.getNumberVouchers();

            // String => Country_code == 'ES' OR 'DE', Integer => counter each country
            HashMap<String, Integer> countryMin = new HashMap<>();
            HashMap<String, Integer> countryMax = new HashMap<>();

            // Countries extracted from list of applications in FIFO order
            List<String> participatingCountries = new ArrayList<>();

            // List<ApplicationDTO> listOfApplications = applicationService.getApplicationsByCallFiFoOrder(call.getId());
            long dateNanoSeconds = call.getStartDate() * 1000000;
            List<ApplicationDTO> listOfApplications = applicationService.findByCallIdOrderByDateBeforeCallDateAsc(call.getId(), dateNanoSeconds);

            _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Initializing municipalities, laus & registrations");

            List<SimpleMunicipalityDTO> municipalities = simpleMunicipalityService.getAllMunicipalities();
            List<SimpleLauDTO> laus = simpleLauService.getAllLausFromApplications();
            List<SimpleRegistrationDTO> registrations = simpleRegistrationService.findAll();

            for (SimpleMunicipalityDTO mun : municipalities) {
                municipalityHashMap.put(mun.getId(), mun);
            }

            for (SimpleRegistrationDTO reg : registrations) {
                registrationsHashMap.put(reg.getId(), reg);
            }

            for (SimpleLauDTO lau : laus) {
                lauHashMap.put(lau.getId(), lau);
                if (!participatingCountries.contains(lau.getCountry_code()) && lau.getCountry_code() != null) {
                    participatingCountries.add(lau.getCountry_code());
                }
            }

            int nrParticipatingCountries = participatingCountries.size();
            if (participatingCountries.isEmpty()) {
                return new ResponseDTO(false, "Simulation stopped", null);
            }

            int numReserveList = call.getReserve();

            // String => name municipality, Application of the municipality
            HashMap<Integer, ApplicationDTO> assignedVouchers = new HashMap<>();

            // String => Country_code == 'ES' OR 'DE', List of applications in reserved list of each country
            HashMap<String, List<ApplicationDTO>> countryReserveList = new HashMap<>();

            // String => Country_code == 'ES' OR 'DE', Integer => counter each country
            HashMap<String, Integer> countryCounter = new HashMap<>();
            HashMap<String, Integer> countryReserveListCounter = new HashMap<>();

            HashMap<String, List<MunicipalityDTO>> countryTest = new HashMap<>();

            List<ApplicationDTO> rejectedApplications = new ArrayList<>();

            int totalAssignedVouchers = 0;

            // Fill maximums and minimums for each country
            List<VoucherManagementDTO> voucherManagements = voucherManagementService.getVoucherManagementByCall(call.getId());

            for (VoucherManagementDTO voucherManagement : voucherManagements) {
                if (voucherManagement.getMaximum() == 0 || voucherManagement.getMinimum() == 0) {
                    return new ResponseDTO(false, "Simulation stopped", null);
                }
                countryTest.put(voucherManagement.getCountryCode().toUpperCase(), new ArrayList<>());
                countryReserveListCounter.put(voucherManagement.getCountryCode().toUpperCase(), 0);
                countryMax.put(voucherManagement.getCountryCode().toUpperCase(), voucherManagement.getMaximum());
                countryMin.put(voucherManagement.getCountryCode().toUpperCase(), voucherManagement.getMinimum());
            }

            for (String country : participatingCountries) {
                if (country != null) {
                    country = country.toUpperCase().trim();
                    countryCounter.put(country, 0);
                }
            }

            // List of applications cloned to use in the algorithm
            List<ApplicationDTO> supportLOAlist = new ArrayList<>(listOfApplications);
            _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Assigning minimum vouchers");

            for (String country : participatingCountries) {

                try {

                    if (country.equalsIgnoreCase("no")) {
                        country = country.toUpperCase();
                    }

                    //GET Applications for each country
                    List<ApplicationDTO> applicationsCountry = applicationService.getApplicationByCallAndCountry(callId, country, dateNanoSeconds);

                    for (ApplicationDTO applicationDTO : applicationsCountry) {

                        if (applicationDTO.getStatus() == 1) {
                            removeFromLOA(supportLOAlist, applicationDTO);
                            continue;
                        }
                        if (applicationDTO.getRejected()) {
                            rejectedApplications.add(applicationDTO);
                            removeFromLOA(supportLOAlist, applicationDTO);
                            continue;
                        }

                        if (countryCounter.get(country) >= countryMin.get(country)) {
                            //removeFromLOA(supportLOAlist, applicationDTO);
                            break;
                        }

                        SimpleRegistrationDTO registrationDTO = registrationsHashMap.get(applicationDTO.getRegistrationId());

                        if (registrationDTO != null) {
                            SimpleMunicipalityDTO municipalityDTO = municipalityHashMap.get(registrationDTO.getMunicipalityId());

                            //Check if municipality with the same name have been assigned, if assigned then delete else continue
                            if (assignedVouchers.containsKey(municipalityDTO.getLau())) {
                                removeFromLOA(supportLOAlist, applicationDTO);
                                continue;
                            }
                            assignedVouchers.put(municipalityDTO.getLau(), applicationDTO);
                            countryCounter.put(country, countryCounter.get(country) + 1);
                            totalAssignedVouchers += 1;
                            removeFromLOA(supportLOAlist, applicationDTO);
                        }
                    }
                } catch (Exception e) {
                    _log.warn("ECAS Username: " + userConnected.getEcasUsername() + " - Error assigning minimum of vouchers to country", e.getMessage());
                }
            }

            _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Assigning maximum vouchers");

            for (ApplicationDTO applicationDTO : new ArrayList<>(supportLOAlist)) {
                try {
                    if (totalAssignedVouchers >= vouchersToBeAssigned || supportLOAlist.size() == 0) {
                        break;
                    }

                    if (applicationDTO.getStatus() == 1) {
                        removeFromLOA(supportLOAlist, applicationDTO);
                        continue;
                    }
                    if (applicationDTO.getRejected()) {
                        rejectedApplications.add(applicationDTO);
                        removeFromLOA(supportLOAlist, applicationDTO);
                        continue;
                    }

                    SimpleRegistrationDTO registrationDTO = registrationsHashMap.get(applicationDTO.getRegistrationId());

                    if (registrationDTO != null) {
                        SimpleMunicipalityDTO municipalityDTO = municipalityHashMap.get(registrationDTO.getMunicipalityId());
                        SimpleLauDTO lauDTO = lauHashMap.get(municipalityDTO.getLau());

                        String country = lauDTO.getCountry_code();
                        country = country.trim();
                        if (country.equalsIgnoreCase("no")) {
                            country = country.toUpperCase();
                        }

                        if (assignedVouchers.containsKey(municipalityDTO.getLau())) {
                            removeFromLOA(supportLOAlist, applicationDTO);
                            continue;
                        }

                        if (countryCounter.get(country) >= countryMax.get(country)) {
                            int numReserveCountry = countryReserveListCounter.get(country);

                            if (numReserveCountry < numReserveList) {
                                if (countryReserveList.get(country) == null) {
                                    countryReserveList.put(country, new ArrayList<>());
                                }
                                List<ApplicationDTO> countryApps = new ArrayList<>(countryReserveList.get(country));
                                countryApps.add(applicationDTO);
                                countryReserveList.put(country, countryApps);
                                countryReserveListCounter.put(lauDTO.getCountry_code(), countryReserveListCounter.get(country) + 1);
                            }
                            removeFromLOA(supportLOAlist, applicationDTO);
                            continue;
                        }
                        assignedVouchers.put(municipalityDTO.getLau(), applicationDTO);
                        countryCounter.put(lauDTO.getCountry_code(), countryCounter.get(lauDTO.getCountry_code()) + 1);
                        totalAssignedVouchers += 1;
                        removeFromLOA(supportLOAlist, applicationDTO);
                    }
                } catch (Exception e) {
                    _log.warn("ECAS Username: " + userConnected.getEcasUsername() + " - Error assigning maximum of vouchers to country", e.getMessage());
                }
            }

            _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Filling reserve list");
            if (!supportLOAlist.isEmpty()) {
                for (String country : participatingCountries) {
                    country = country.trim();
                    if (country.equalsIgnoreCase("no")) {
                        country = country.toUpperCase();
                    }

                    if (countryReserveListCounter.get(country) >= numReserveList) {
                        continue;
                    }

                    try {
                        List<ApplicationDTO> applicationsCountry = getFirtsApplicationCountry(registrationsHashMap, lauHashMap, municipalityHashMap,
                                supportLOAlist, country);

                        for (ApplicationDTO application : applicationsCountry) {

                            if (application.getStatus() == 1) {
                                removeFromLOA(supportLOAlist, application);
                                continue;
                            }
                            if (application.getRejected()) {
                                rejectedApplications.add(application);
                                removeFromLOA(supportLOAlist, application);
                                continue;
                            }


                            SimpleRegistrationDTO registrationDTO = registrationsHashMap.get(application.getRegistrationId());

                            if (registrationDTO != null) {
                                SimpleMunicipalityDTO municipalityDTO = municipalityHashMap.get(registrationDTO.getMunicipalityId());

                                List<ApplicationDTO> countryApps = countryReserveList.get(country);

                                if (countryApps == null) {
                                    countryApps = new ArrayList<>();
                                }

                                if (assignedVouchers.containsKey(municipalityDTO.getLau()) || checkIfApplicationExists(countryApps, application)) {
                                    removeFromLOA(supportLOAlist, application);
                                    continue;
                                }

                                if (countryReserveListCounter.get(country) >= numReserveList) {
                                    break;
                                }

                                countryApps.add(application);
                                countryReserveList.put(country, countryApps);
                                countryReserveListCounter.put(country, countryReserveListCounter.get(country) + 1);

                                removeFromLOA(supportLOAlist, application);
                            }
                        }
                    } catch (Exception e) {
                        _log.warn("ECAS Username: " + userConnected.getEcasUsername() + " - Error assigning reserve of vouchers to country", e.getMessage());
                    }
                }
            }

            List<List<ApplicationDTO>> generalOutput = new ArrayList<>();
            List<ApplicationDTO> mainListOutput = new ArrayList<>(assignedVouchers.values());
            Set<VoucherSimulationDTO> simulations = new HashSet<>();

            List<ApplicationDTO> reserveListCompleted = new ArrayList<>();

            for (List<ApplicationDTO> applications : countryReserveList.values()) {
                reserveListCompleted.addAll(applications);
            }

            Collections.sort(mainListOutput, (o1, o2) -> {
                if (o1.getDate() > o2.getDate()) {
                    return 1;
                } else if (o1.getDate() < o2.getDate()) {
                    return -1;
                } else {
                    return 0;
                }
            });

            Collections.sort(reserveListCompleted, (o1, o2) -> {
                if (o1.getDate() > o2.getDate()) {
                    return 1;
                } else if (o1.getDate() < o2.getDate()) {
                    return -1;
                } else {
                    return 0;
                }
            });

            VoucherAssignmentDTO assignmentDTO = new VoucherAssignmentDTO();
            assignmentDTO.setCall(call);
            assignmentDTO.setStatus(1);
            assignmentDTO.setExecutionDate(new Date().getTime());
            assignmentDTO.setUser(userDTO);

            VoucherAssignmentDTO voucherAssignment = getVoucherAssignmentByCall(call.getId());

            if (voucherAssignment == null) {
                voucherAssignment = voucherAssignmentMapper.toDTO(voucherAssignmentRepository.save(voucherAssignmentMapper.toEntity(assignmentDTO)));
            } else {
                voucherSimulationRepository.deleteVoucherSimulationByVoucherAssignment(voucherAssignment.getId());

                voucherAssignment.setCall(call);
                voucherAssignment.setUser(userDTO);
                voucherAssignment.setVoucherSimulations(null);
                voucherAssignmentRepository.save(voucherAssignmentMapper.toEntity(voucherAssignment));
            }


            HashMap<String, Integer> countryRankCounter = new HashMap<>();
            int i = 1;
            for (ApplicationDTO applicationAssigned : mainListOutput) {

                SimpleRegistrationDTO registrationDTO = registrationsHashMap.get(applicationAssigned.getRegistrationId());
                SimpleMunicipalityDTO municipalityDTO = municipalityHashMap.get(registrationDTO.getMunicipalityId());

                int num = applicationService.countApplicationWithSameMunicipalityName(municipalityDTO.getLau(), call.getId(), dateNanoSeconds);

                countryRankCounter.put(municipalityDTO.getCountry(), countryRankCounter.getOrDefault(municipalityDTO.getCountry(), 0) + 1);

                VoucherSimulationDTO simulation = new VoucherSimulationDTO();
                simulation.setCountry(municipalityDTO.getCountry());
                simulation.setApplication(applicationAssigned);
                simulation.setVoucherAssignment(voucherAssignment.getId());
                simulation.setNumApplications(num);
                simulation.setMunicipality(municipalityDTO.getId());
                simulation.setEuRank(i);
                simulation.setSelectionStatus(0);
                simulation.setCountryRank(countryRankCounter.get(municipalityDTO.getCountry()));
                simulations.add(simulation);
                i++;
            }

            for (ApplicationDTO reservedApplication : reserveListCompleted) {
                SimpleRegistrationDTO registrationDTO = registrationsHashMap.get(reservedApplication.getRegistrationId());
                SimpleMunicipalityDTO municipalityDTO = municipalityHashMap.get(registrationDTO.getMunicipalityId());

                int num = applicationService.countApplicationWithSameMunicipalityName(municipalityDTO.getLau(), call.getId(), dateNanoSeconds);

                countryRankCounter.put(municipalityDTO.getCountry(), countryRankCounter.getOrDefault(municipalityDTO.getCountry(), 0) + 1);

                VoucherSimulationDTO simulation = new VoucherSimulationDTO();
                simulation.setApplication(reservedApplication);
                simulation.setCountry(municipalityDTO.getCountry());
                simulation.setVoucherAssignment(voucherAssignment.getId());
                simulation.setNumApplications(num);
                simulation.setMunicipality(municipalityDTO.getId());
                simulation.setEuRank(i);
                simulation.setSelectionStatus(1);
                simulation.setCountryRank(countryRankCounter.get(municipalityDTO.getCountry()));
                simulations.add(simulation);
                i++;
            }

            for (ApplicationDTO rejectedApplication : rejectedApplications) {
                SimpleRegistrationDTO registrationDTO = registrationsHashMap.get(rejectedApplication.getRegistrationId());
                SimpleMunicipalityDTO municipalityDTO = municipalityHashMap.get(registrationDTO.getMunicipalityId());

                int num = applicationService.countApplicationWithSameMunicipalityName(municipalityDTO.getLau(), call.getId(), dateNanoSeconds);

                countryRankCounter.put(municipalityDTO.getCountry(), countryRankCounter.getOrDefault(municipalityDTO.getCountry(), 0) + 1);

                VoucherSimulationDTO simulation = new VoucherSimulationDTO();
                simulation.setApplication(rejectedApplication);
                simulation.setCountry(municipalityDTO.getCountry());
                simulation.setVoucherAssignment(voucherAssignment.getId());
                simulation.setNumApplications(num);
                simulation.setMunicipality(municipalityDTO.getId());
                simulation.setEuRank(Integer.MAX_VALUE);
                simulation.setSelectionStatus(2);
                simulation.setCountryRank(Integer.MAX_VALUE);
                simulations.add(simulation);
                i++;
            }
            _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Saving simulation to database");

            voucherAssignment.setVoucherSimulations(simulations);
            VoucherAssignmentDTO res = voucherAssignmentMapper.toDTO(voucherAssignmentRepository.save(voucherAssignmentMapper.toEntity(voucherAssignment)));
            VoucherAssignmentAuxiliarDTO voucher = new VoucherAssignmentAuxiliarDTO();
            voucher.setId(res.getId());
            voucher.setStatus(res.getStatus());
            voucher.setExecutionDate(res.getExecutionDate());
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + " - Voucher simulation successfully executed");
            return new ResponseDTO(true, voucher, null);
        }
        return new ResponseDTO(false, "User not defined", null);
    }

    public List<ApplicationDTO> getFirtsApplicationCountry(HashMap<Integer, SimpleRegistrationDTO> registrationsMap,
                                                           HashMap<Integer, SimpleLauDTO> lausMap,
                                                           HashMap<Integer, SimpleMunicipalityDTO> municipalitiesMap,
                                                           List<ApplicationDTO> apps, String country) {
        List<ApplicationDTO> appCountry = new ArrayList<>();
        for (ApplicationDTO application : apps) {
            SimpleRegistrationDTO registrationDTO = registrationsMap.get(application.getRegistrationId());
            if (registrationDTO != null) {
                SimpleMunicipalityDTO municipalityDTO = municipalitiesMap.get(registrationDTO.getMunicipalityId());

                try {
                    SimpleLauDTO lauDTO = lausMap.get(municipalityDTO.getLau());
                    if (lauDTO.getCountry_code().equals(country)) {
                        appCountry.add(application);
                    }
                } catch (Exception e) {
                    _log.warn("ECAS Username: " + userConnected.getEcasUsername() + " - Error retrieving application by country", e.getMessage());
                }
            }
        }
        return appCountry;
    }

    public void removeFromLOA(List<ApplicationDTO> listLOA, ApplicationDTO applicationDTO) {
        for (int i = 0; i < listLOA.size(); i++) {
            if (listLOA.get(i).getId() == applicationDTO.getId()) {
                listLOA.remove(i);
                break;
            }
        }
    }

    public boolean checkIfApplicationExists(List<ApplicationDTO> listAppsCountry, ApplicationDTO application) {
        for (ApplicationDTO applicationCountry : listAppsCountry) {
            if (applicationCountry.getId() == application.getId()) {
                return true;
            }
        }
        return false;
    }

    public void sendNotificationForApplicants(int callId) {

        CallDTO callDTO = callService.getCallById(callId);

        if (callDTO == null) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + " - Call does not exist with id " + callId);
            throw new AppException("Call not found with id: " + callId);
        }

        List<ApplicationDTO> successfulApplicants;
        List<ApplicationDTO> reserveApplicants;
        List<ApplicationDTO> unsuccessfulApplicants = new ArrayList<>();

        VoucherAssignmentAuxiliarDTO finalVoucherAssignment = getVoucherAssignmentByCallAndStatus(callId, VoucherAssignmentStatus.FREEZE_LIST.getValue());

        if (finalVoucherAssignment == null) {
            throw new AppException("Notification could not be sent because there's no freeze list for call with id : " + callId);
        }

        successfulApplicants = applicationMapper.toDTOList(applicationRepository.getApplicationsSelectedInVoucherAssignment(finalVoucherAssignment.getId(), SelectionStatus.SELECTED.getValue()));
        reserveApplicants = applicationMapper.toDTOList(applicationRepository.getApplicationsSelectedInVoucherAssignment(finalVoucherAssignment.getId(), SelectionStatus.RESERVE_LIST.getValue()));
        unsuccessfulApplicants.addAll(applicationMapper.toDTOList(applicationRepository.getApplicationsSelectedInVoucherAssignment(finalVoucherAssignment.getId(), SelectionStatus.REJECTED.getValue())));
        unsuccessfulApplicants.addAll(applicationMapper.toDTOList(applicationRepository.getApplicationsNotSelectedInVoucherAssignment(callDTO.getId(), finalVoucherAssignment.getId())));

        Locale locale = new Locale(UserConstants.DEFAULT_LANG);
        String subject;
        String msgBody;

        for (ApplicationDTO successfulApplicant : successfulApplicants) {
            RegistrationDTO registrationDTO = registrationService.getRegistrationById(successfulApplicant.getRegistrationId());
            UserDTO userDTO = userService.getUserById(registrationDTO.getUserId());
            if (userDTO.getLang() != null) {
                locale = new Locale(userDTO.getLang());
            }
            ResourceBundle bundle = ResourceBundle.getBundle("MailBundle", locale);
            subject = bundle.getString("mail.dgConn.voucherAssignment.subject");
            msgBody = bundle.getString("mail.dgConn.voucherAssignment.successfulApplicant.body");
            String additionalInfoUrl = userService.getBaseUrl() + "beneficiary-portal/my-voucher";
//            subject = MessageFormat.format(subject, successfulApplicant.getCallId());
            subject = MessageFormat.format(subject, callDTO.getEvent());
            msgBody = MessageFormat.format(msgBody, additionalInfoUrl);
            // TODO: Change it to work with CNS
            if (!userService.isLocalHost()) {
                //mailService.sendEmailAsync(userDTO.getEmail(), MailService.FROM_ADDRESS, subject, msgBody);
            }
        }
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Email sended to " + successfulApplicants.size() + " successful applicants");

        for (ApplicationDTO reserveApplicant : reserveApplicants) {
            RegistrationDTO registrationDTO = registrationService.getRegistrationById(reserveApplicant.getRegistrationId());
            UserDTO userDTO = userService.getUserById(registrationDTO.getUserId());
            if (userDTO.getLang() != null) {
                locale = new Locale(userDTO.getLang());
            }
            ResourceBundle bundle = ResourceBundle.getBundle("MailBundle", locale);
            subject = bundle.getString("mail.dgConn.voucherAssignment.subject");
            msgBody = bundle.getString("mail.dgConn.voucherAssignment.reserveApplicant.body");
            String additionalInfoUrl = userService.getBaseUrl();
            subject = MessageFormat.format(subject, callDTO.getEvent());
            msgBody = MessageFormat.format(msgBody, additionalInfoUrl);
            // TODO: Change it to work with CNS
            if (!userService.isLocalHost()) {
                // mailService.sendEmailAsync(userDTO.getEmail(), MailService.FROM_ADDRESS, subject, msgBody);
            }
        }
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Email sended to " + reserveApplicants.size() + " reserve applicants");

        for (ApplicationDTO unsuccessfulApplicant : unsuccessfulApplicants) {
            RegistrationDTO registrationDTO = registrationService.getRegistrationById(unsuccessfulApplicant.getRegistrationId());
            UserDTO userDTO = userService.getUserById(registrationDTO.getUserId());
            if (userDTO.getLang() != null) {
                locale = new Locale(userDTO.getLang());
            }
            ResourceBundle bundle = ResourceBundle.getBundle("MailBundle", locale);
            subject = bundle.getString("mail.dgConn.voucherAssignment.subject");
            msgBody = bundle.getString("mail.dgConn.voucherAssignment.unsuccesfulApplicant.body");
            String option;

            if (unsuccessfulApplicant.getInvalidateReason() != null && !unsuccessfulApplicant.getInvalidateReason().isEmpty()) {
                option = bundle.getString("mail.dgConn.voucherAssignment.unsuccesfulApplicant.option1");
            } else {
                option = bundle.getString("mail.dgConn.voucherAssignment.unsuccesfulApplicant.option2");
            }

            msgBody = MessageFormat.format(msgBody, option);
            subject = MessageFormat.format(subject, callDTO.getEvent());
            // TODO: Change it to work with CNS
            if (!userService.isLocalHost()) {
                // mailService.sendEmailAsync(userDTO.getEmail(), MailService.FROM_ADDRESS, subject, msgBody);
            }
        }
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Email sended to " + unsuccessfulApplicants.size() + " unsuccessful applicants");

        VoucherAssignment voucherAssignment = voucherAssignmentRepository.findByCallIdAndStatusEquals(callDTO.getId(), VoucherAssignmentStatus.FREEZE_LIST.getValue());
        voucherAssignment.setNotifiedDate(new Date().getTime());
        voucherAssignmentRepository.save(voucherAssignment);
    }

}