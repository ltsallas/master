package wifi4eu.wifi4eu.service.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.common.dto.model.*;
import wifi4eu.wifi4eu.mapper.thread.ThreadMessageMapper;
import wifi4eu.wifi4eu.mapper.user.UserMapper;
import wifi4eu.wifi4eu.repository.thread.ThreadMessageRepository;
import wifi4eu.wifi4eu.repository.user.UserRepository;
import wifi4eu.wifi4eu.service.municipality.MunicipalityService;
import wifi4eu.wifi4eu.service.supplier.SupplierService;
import wifi4eu.wifi4eu.service.user.UserConstants;
import wifi4eu.wifi4eu.service.user.UserService;
import wifi4eu.wifi4eu.util.MailService;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class ThreadMessageService {
    @Autowired
    ThreadMessageMapper threadMessageMapper;

    @Autowired
    ThreadMessageRepository threadMessageRepository;

    @Autowired
    ThreadService threadService;

    @Autowired
    MunicipalityService municipalityService;

    @Autowired
    SupplierService supplierService;

    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    public ThreadMessageDTO createThreadMessage(ThreadMessageDTO threadMessageDTO) {
        ThreadMessageDTO threadMessage = threadMessageMapper.toDTO(threadMessageRepository.save(threadMessageMapper.toEntity(threadMessageDTO)));
        ThreadDTO thread = threadService.getThreadById(threadMessage.getThreadId());
        if (thread.getType() == 1) {
            List<MunicipalityDTO> municipalities = municipalityService.getMunicipalitiesByLauId(Integer.valueOf(thread.getReason()));
            if (municipalities.size() <= 10) {
                if (!userService.isLocalHost()) {
                    for (MunicipalityDTO municipality : municipalities) {
                        UserDTO user = userMapper.toDTO(userRepository.findMainUserFromRegistration(municipality.getRegistrations().get(0).getId()));
                        if (user != null) {
                            Locale locale = new Locale(UserConstants.DEFAULT_LANG);
                            if (user.getLang() != null) {
                                locale = new Locale(user.getLang());
                            }
                            ResourceBundle bundle = ResourceBundle.getBundle("MailBundle", locale);
                            String subject = bundle.getString("mail.thread.subject");
                            String msgBody = bundle.getString("mail.thread.body");
                            mailService.sendEmail(user.getEcasEmail(), MailService.FROM_ADDRESS, subject, msgBody, municipality.getId(), "createThreadMessage");
                        }
                    }
                }
            }
        }
        return threadMessage;
    }
}