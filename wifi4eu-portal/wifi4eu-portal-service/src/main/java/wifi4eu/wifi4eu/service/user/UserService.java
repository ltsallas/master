package wifi4eu.wifi4eu.service.user;

import com.google.common.collect.Lists;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import wifi4eu.wifi4eu.common.Constant;
import wifi4eu.wifi4eu.common.dto.model.*;
import wifi4eu.wifi4eu.common.dto.security.ActivateAccountDTO;
import wifi4eu.wifi4eu.common.dto.security.TempTokenDTO;
import wifi4eu.wifi4eu.common.ecas.UserHolder;
import wifi4eu.wifi4eu.common.exception.AppException;
import wifi4eu.wifi4eu.common.security.TokenGenerator;
import wifi4eu.wifi4eu.common.security.UserContext;
import wifi4eu.wifi4eu.entity.security.RightConstants;
import wifi4eu.wifi4eu.entity.security.TempToken;
import wifi4eu.wifi4eu.mapper.security.TempTokenMapper;
import wifi4eu.wifi4eu.mapper.supplier.SuppliedRegionMapper;
import wifi4eu.wifi4eu.mapper.supplier.SupplierMapper;
import wifi4eu.wifi4eu.mapper.user.UserMapper;
import wifi4eu.wifi4eu.repository.security.TempTokenRepository;
import wifi4eu.wifi4eu.repository.supplier.SuppliedRegionRepository;
import wifi4eu.wifi4eu.repository.supplier.SupplierRepository;
import wifi4eu.wifi4eu.repository.user.UserRepository;
import wifi4eu.wifi4eu.service.municipality.MunicipalityService;
import wifi4eu.wifi4eu.service.security.PermissionChecker;
import wifi4eu.wifi4eu.service.supplier.SupplierService;
import wifi4eu.wifi4eu.service.thread.UserThreadsService;
import wifi4eu.wifi4eu.util.MailService;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
@PropertySource("classpath:env.properties")
@Service
public class UserService {
    private final Logger _log = LogManager.getLogger(UserService.class);

    @Value("${mail.server.location}")
    private String baseUrl;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TempTokenMapper tempTokenMapper;

    @Autowired
    TempTokenRepository tempTokenRepository;

    @Autowired
    MailService mailService;

    @Autowired
    PermissionChecker permissionChecker;

    @Autowired
    MunicipalityService municipalityService;

    @Autowired
    SupplierService supplierService;

    @Autowired
    SupplierMapper supplierMapper;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SuppliedRegionMapper suppliedRegionMapper;

    @Autowired
    SuppliedRegionRepository suppliedRegionRepository;

    @Autowired
    UserThreadsService userThreadsService;

    public List<UserDTO> getAllUsers() {
        return userMapper.toDTOList(Lists.newArrayList(userRepository.findAll()));
    }

    public UserDTO getUserById(int userId) {
        return userMapper.toDTO(userRepository.findOne(userId));
    }

    public UserDTO getUserByEmail(String email) {
        return userMapper.toDTO(userRepository.findByEmail(email));
    }

    public UserDTO getUserByEcasEmail(String email){
        return userMapper.toDTO(userRepository.findByEcasEmail(email));
    }

    public String getIp(HttpServletRequest request){
        String ipAdd = request.getHeader("X-FORWARDED-FOR");
        if(ipAdd == null){
            ipAdd = request.getRemoteAddr();
        }
        return ipAdd;
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) throws Exception {
        UserDTO searchUser = getUserByEmail(userDTO.getEcasEmail());
        if (searchUser != null) {
            userDTO.setPassword(searchUser.getPassword());
            throw new Exception("User already registered.");
        }
        UserDTO resUser = userMapper.toDTO(userRepository.save(userMapper.toEntity(userDTO)));
        sendActivateAccountMail(resUser);
        return resUser;
    }

    @Transactional
    public UserDTO saveUserChanges(UserDTO userDTO) throws Exception {
        UserDTO searchUser = getUserById(userDTO.getId());
        if (searchUser != null) {
            userDTO.setPassword(searchUser.getPassword());
            UserDTO resUser = userMapper.toDTO(userRepository.save(userMapper.toEntity(userDTO)));
            permissionChecker.addTablePermissions(userDTO, Integer.toString(resUser.getId()),
                    RightConstants.USER_TABLE, "[USER] - id: " + userDTO.getId() + " - Email: " + userDTO.getEcasEmail() + " - EcasUsername: " + userDTO.getEcasUsername());
            return resUser;
        } else {
            throw new Exception("User doesn't exist.");
        }
    }

    @Transactional
    public UserDTO getUserByUserContext(UserContext userContext) {

        if (userContext == null) {
            throw new AppException("User context not defined", HttpStatus.SC_FORBIDDEN, "");
        }

        if(_log.isDebugEnabled()){
            _log.debug("user Email: " + userContext.getEmail() + " user PerId: " + userContext.getPerId());
        }

        UserDTO userDTO = userMapper.toDTO(userRepository.findByEcasUsername(userContext.getUsername()));
        if (userDTO == null) {
            userDTO = new UserDTO();
            userDTO.setAccessDate(new Date().getTime());
            userDTO.setEcasEmail(userContext.getEmail());
            userDTO.setEcasUsername(userContext.getUsername());
            userDTO.setName(userContext.getFirstName());
            userDTO.setSurname(userContext.getLastName());
            userDTO.setEmail(userContext.getEmail());

            userDTO = userMapper.toDTO(userRepository.save(userMapper.toEntity(userDTO)));

            permissionChecker.addTablePermissions(userDTO, Integer.toString(userDTO.getId()),
                    RightConstants.USER_TABLE, "[USER] - id: " + userDTO.getId() + " - Email: " + userDTO.getEcasEmail() + " - EcasUsername: " + userDTO.getEcasUsername());

        }
        return userDTO;
    }

    @Transactional
    public UserDTO deleteUser(int userId, HttpServletRequest request) {
        UserDTO userDTO = userMapper.toDTO(userRepository.findOne(userId));
        if (userDTO != null) {
            switch (userDTO.getType()) {
                case (int) Constant.ROLE_REPRESENTATIVE:
                    removeTempToken(userDTO);
                    for (MunicipalityDTO municipality : municipalityService.getMunicipalitiesByUserId(userDTO.getId())) {
                        municipalityService.deleteMunicipality(municipality.getId(), request);
                    }
                    for (UserThreadsDTO userThread : userThreadsService.getUserThreadsByUserId(userDTO.getId())) {
                        userThreadsService.deleteUserThreads(userThread.getId());
                    }
                    break;
                case (int) Constant.ROLE_SUPPLIER:
                    removeTempToken(userDTO);
                    removeSuppliedRegion(userDTO);

                    SupplierDTO supplier = supplierService.getSupplierByUserId(userDTO.getId());
                    if (supplier != null) {
                        supplierService.deleteSupplier(supplier.getId());
                    }
                    break;
            }
            userRepository.delete(userMapper.toEntity(userDTO));
            return userDTO;
        } else {
            return null;
        }
    }

    @Transactional
    public UserDTO updateUserDetails(UserDTO userDTO, String name, String surname) {

        userDTO.setName(name);
        userDTO.setSurname(surname);

        return userMapper.toDTO(userRepository.save(userMapper.toEntity(userDTO)));
    }

    public List<UserDTO> getUsersByType(int type) {
        return userMapper.toDTOList(Lists.newArrayList(userRepository.findByType(type)));
    }

    public UserDTO login(UserDTO userDTO) {
        UserDTO resUser = userMapper.toDTO(userRepository.findByEmail(userDTO.getEcasEmail()));
        if (resUser != null && userDTO.getPassword().equals(resUser.getPassword())) {
            resUser.setAccessDate(new Date().getTime());
            resUser = userMapper.toDTO(userRepository.save(userMapper.toEntity(resUser)));
            return resUser;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void activateAccount(ActivateAccountDTO activateAccountDTO) throws Exception {
        TempTokenDTO tempToken = tempTokenMapper.toDTO(tempTokenRepository.findByToken(activateAccountDTO.getToken()));
        Date now = new Date();
        if (tempToken != null) {
            if (new Date(tempToken.getExpiryDate()).after(now)) {
                UserContext userContext = UserHolder.getUser();
                UserDTO user;
                if (userContext != null) {
                    //ECAS user
                    user = getUserByUserContext(userContext);
                    if (tempToken.getUserId() != user.getId()) {
                        throw new Exception("Token doesn't match with the ECAS user");
                    }
                } else {
                    //
                    user = userMapper.toDTO(userRepository.findOne(tempToken.getUserId()));
                    user.setPassword(activateAccountDTO.getPassword());
                }
                user.setVerified(true);
                userRepository.save(userMapper.toEntity(user));
            } else {
                throw new Exception("Token has expired.");
            }
        } else {
            throw new Exception("Token doesn't exist.");
        }
    }

    @Transactional
    public Cookie getCSRFCookie() throws AppException {
        _log.debug("[i] getUserByEcasPerId");

        Cookie cookie = null;
        cookie = new Cookie("XSRF-TOKEN", generateCSRFToken());
        cookie.setSecure(true);
        cookie.setMaxAge(365 * 24 * 60 * 60);
        cookie.setPath("/");

        _log.debug("[f] getUserByEcasPerId");
        return cookie;
    }

    private String generateCSRFToken() throws AppException{
        String token = new TokenGenerator().generate();

        if (token != null && token.length() > 0) {
            UserContext userContext = UserHolder.getUser();
            UserDTO user;
            if (userContext != null) {
                user = getUserByUserContext(userContext);
                if (user != null) {
                    user.setCsrfToken(token);
                    userRepository.save(userMapper.toEntity(user));
                } else {
                    throw new AppException("Contact your administrator", HttpStatus.SC_INTERNAL_SERVER_ERROR, "");
                }
            } else {
                throw new AppException("Contact your administrator", HttpStatus.SC_INTERNAL_SERVER_ERROR, "");
            }
        } else {
            throw new AppException("Contact your administrator", HttpStatus.SC_INTERNAL_SERVER_ERROR, "");
        }

        return token;
    }

    @Transactional
    public void sendActivateAccountMail(UserDTO userDTO) {
        Date now = new Date();
        TempTokenDTO tempTokenDTO = new TempTokenDTO();
        tempTokenDTO.setEmail(userDTO.getEcasEmail());
        tempTokenDTO.setUserId(userDTO.getId());
        tempTokenDTO.setCreateDate(now.getTime());
        tempTokenDTO.setExpiryDate(DateUtils.addHours(now, UserConstants.TIMEFRAME_ACTIVATE_ACCOUNT_HOURS).getTime());
        SecureRandom secureRandom = new SecureRandom();
        String token = Long.toString(secureRandom.nextLong()).concat(Long.toString(now.getTime())).replaceAll("-", "");
        tempTokenDTO.setToken(token);
        tempTokenDTO = tempTokenMapper.toDTO(tempTokenRepository.save(tempTokenMapper.toEntity(tempTokenDTO)));
        permissionChecker.addTablePermissions(userDTO, Long.toString(tempTokenDTO.getId()),
                RightConstants.TEMP_TOKENS_TABLE, "[TEMP_TOKENS] - id: " + tempTokenDTO.getId() + " - User Id: " + tempTokenDTO.getUserId() + " - TOKEN: " + tempTokenDTO.getToken());

        Locale locale = new Locale(UserConstants.DEFAULT_LANG);
        if (userDTO.getLang() != null) {
            locale = new Locale(userDTO.getLang());
        }
        ResourceBundle bundle = ResourceBundle.getBundle("MailBundle", locale);
        String subject = bundle.getString("mail.subject");
        String msgBody = bundle.getString("mail.body");

        if (!isLocalHost()) {
            mailService.sendEmail(userDTO.getEcasEmail(), MailService.FROM_ADDRESS, subject, msgBody);
        }
    }

    public boolean resendEmail(String email) {
        UserDTO userDTO = userMapper.toDTO(userRepository.findByEmail(email));
        if (userDTO != null) {
            sendActivateAccountMail(userDTO);
            return true;
        } else {
            return false;
        }
    }


    public void forgotPassword(String email) throws Exception {

        UserContext userContext = UserHolder.getUser();
        UserDTO user = getUserByUserContext(userContext);

        if (user == null) {
            /* validate email variable is not null or empty */
            if (email != null && !StringUtils.isEmpty(email)) {
                UserDTO userDTO = userMapper.toDTO(userRepository.findByEmail(email));
                /* validate if user exist in wifi4eu portal */
                if (userDTO != null) {
                    /* Create a temporal key for activation and reset password functionalities */
                    TempTokenDTO tempTokenDTO = tempTokenMapper.toDTO(tempTokenRepository.findByEmail(email));
                    if (tempTokenDTO == null) {
                        tempTokenDTO = new TempTokenDTO();
                        tempTokenDTO.setEmail(email);
                        tempTokenDTO.setUserId(userDTO.getId());
                    }
                    Date now = new Date();
                    tempTokenDTO.setCreateDate(now.getTime());
                    tempTokenDTO.setExpiryDate(DateUtils.addHours(now, UserConstants.TIMEFRAME_ACTIVATE_ACCOUNT_HOURS).getTime());
                    SecureRandom secureRandom = new SecureRandom();
                    String token = Long.toString(secureRandom.nextLong()).concat(Long.toString(now.getTime())).replaceAll("-", "");
                    tempTokenDTO.setToken(token);

                    tempTokenRepository.save(tempTokenMapper.toEntity(tempTokenDTO));

                    /* Send email with */
                    String fromAddress = MailService.FROM_ADDRESS;
                    //TODO: translate subject and msgBody
                    String subject = "wifi4eu portal Forgot Password";
                    String msgBody = "you can access to the next link and reset your password " + baseUrl + UserConstants.RESET_PASS_URL + tempTokenDTO.getToken();
                    mailService.sendEmail(email, fromAddress, subject, msgBody);
                } else {
                    throw new Exception("trying to forgetPassword with an unregistered user");
                }
            } else {
                throw new Exception("trying to forgetPassword without an email");
            }
        } else {
            throw new Exception("ECAS user has to go throw ECAS portal to manage the password");
        }
    }

    public boolean isLocalHost() {
        return baseUrl.contains(UserConstants.LOCAL);
    }

    public String getLogoutEnviroment() {
        UserContext userContext = UserHolder.getUser();

        if (!UserConstants.MOCKED_MAIL.equals(userContext.getEmail())) {
            return "https://ecas.ec.europa.eu/cas/logout";
        } else {
            return "http://localhost:8080/wifi4eu/#/beneficiary-registration";
        }
    }

    public String getChangePassword() {
        return "https://ecas.ec.europa.eu/cas/change/changePassword.cgi";
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private void removeTempToken(UserDTO userDTO) {
        for (TempToken tempToken : tempTokenRepository.findByUserId(userDTO.getId())) {
            tempTokenRepository.delete(tempToken);
        }
    }

    private void removeSuppliedRegion(UserDTO userDTO) {
        SupplierDTO supplierDTO = supplierMapper.toDTO(supplierRepository.findByUserId(userDTO.getId()));
        List<SuppliedRegionDTO> suppliedRegionDTOList = supplierDTO.getSuppliedRegions();
        for (SuppliedRegionDTO anElementList : suppliedRegionDTOList) {
            suppliedRegionRepository.delete(suppliedRegionMapper.toEntity(anElementList));
        }
    }
}