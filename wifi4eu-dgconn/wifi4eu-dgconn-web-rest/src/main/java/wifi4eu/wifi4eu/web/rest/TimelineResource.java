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
import wifi4eu.wifi4eu.common.dto.model.TimelineDTO;
import wifi4eu.wifi4eu.common.dto.model.UserDTO;
import wifi4eu.wifi4eu.common.dto.rest.ErrorDTO;
import wifi4eu.wifi4eu.common.dto.rest.ResponseDTO;
import wifi4eu.wifi4eu.common.ecas.UserHolder;
import wifi4eu.wifi4eu.service.timeline.TimelineService;
import wifi4eu.wifi4eu.service.user.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@Api(value = "/timeline", description = "Timeline object REST API services")
@RequestMapping("timeline")
public class TimelineResource {
    @Autowired
    private TimelineService timelineService;

    @Autowired
    private UserService userService;

    Logger _log = LoggerFactory.getLogger(CallResource.class);

    /*
    @ApiOperation(value = "Get all the timeline entries")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<TimelineDTO> allTimelines() {
        return timelineService.getAllTimelines();
    }

    @ApiOperation(value = "Get timeline by specific id")
    @RequestMapping(value = "/{timelineId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public TimelineDTO getTimelineById(@PathVariable("timelineId") final Integer timelineId) {
        _log.info("getTimelineById: " + timelineId);
        return timelineService.getTimelineById(timelineId);
    }

    @ApiOperation(value = "Create timeline")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseDTO createTimeline(@RequestBody final TimelineDTO timelineDTO, HttpServletResponse response) {
        try {
            UserDTO userDTO = userService.getUserByUserContext(UserHolder.getUser());
            TimelineDTO resTimeline = new TimelineDTO();
            if(userDTO.getType() != 5){
                response.sendError(HttpStatus.NOT_FOUND.value());
            }else {
                _log.info("createTimeline");
                resTimeline = timelineService.createTimeline(timelineDTO);
            }
            return new ResponseDTO(true, resTimeline, null);
        } catch (Exception e) {
            if (_log.isErrorEnabled()) {
                _log.error("Error on 'createTimeline' operation.", e);
            }
            return new ResponseDTO(false, null, new ErrorDTO(0, e.getMessage()));
        }
    }


    @ApiOperation(value = "Delete timeline by specific id")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseDTO deleteTimeline(@RequestBody final Integer timelineId, HttpServletResponse response) throws IOException {
        try {
            UserDTO userDTO = userService.getUserByUserContext(UserHolder.getUser());
            if(userDTO.getType() != 5){
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            _log.info("deleteTimeline: " + timelineId);
            TimelineDTO resTimeline = timelineService.deleteTimeline(timelineId);
            return new ResponseDTO(true, resTimeline, null);
        }
        catch (AccessDeniedException ade) {
            response.sendError(HttpStatus.NOT_FOUND.value());
            return new ResponseDTO(false, null, new ErrorDTO(HttpStatus.NOT_FOUND.value(), ade.getMessage()));
        }
        catch (Exception e) {
            if (_log.isErrorEnabled()) {
                _log.error("Error on 'deleteTimeline' operation.", e);
            }
            return new ResponseDTO(false, null, new ErrorDTO(0, e.getMessage()));
        }
    }

    */
}