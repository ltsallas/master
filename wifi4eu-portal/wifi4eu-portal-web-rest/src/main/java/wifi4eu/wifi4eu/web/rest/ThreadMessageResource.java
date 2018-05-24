package wifi4eu.wifi4eu.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wifi4eu.wifi4eu.common.dto.model.ThreadMessageDTO;
import wifi4eu.wifi4eu.common.dto.model.UserDTO;
import wifi4eu.wifi4eu.common.dto.rest.ErrorDTO;
import wifi4eu.wifi4eu.common.dto.rest.ResponseDTO;
import wifi4eu.wifi4eu.common.ecas.UserHolder;
import wifi4eu.wifi4eu.service.security.PermissionChecker;
import wifi4eu.wifi4eu.service.thread.ThreadMessageService;
import wifi4eu.wifi4eu.service.thread.UserThreadsService;
import wifi4eu.wifi4eu.service.user.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@Api(value = "/thread/messages", description = "ThreadMessage object REST API services")
@RequestMapping("thread/messages")
public class ThreadMessageResource {
    @Autowired
    private ThreadMessageService threadMessageService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserThreadsService userThreadsService;

    @Autowired
    private PermissionChecker permissionChecker;

    Logger _log = LoggerFactory.getLogger(CallResource.class);

    @ApiOperation(value = "Create thread message")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseDTO createThreadMessage(@RequestBody final ThreadMessageDTO threadMessageDTO, HttpServletResponse response) throws IOException {
        _log.info("createThreadMessage");
        try {
            UserDTO user = userService.getUserByUserContext(UserHolder.getUser());
            if (userThreadsService.getByUserIdAndThreadId(user.getId(), threadMessageDTO.getThreadId()) == null && !permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            if(threadMessageDTO.getAuthorId() != user.getId()){
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            ThreadMessageDTO resThreadMessage = threadMessageService.createThreadMessage(threadMessageDTO);
            return new ResponseDTO(true, resThreadMessage, null);
        } catch (AccessDeniedException ade) {
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        } catch (Exception e) {
            if (_log.isErrorEnabled()) {
                _log.error("Error on 'createThreadMessage' operation.", e);
            }
            return new ResponseDTO(false, null, new ErrorDTO(0, e.getMessage()));
        }
    }
}