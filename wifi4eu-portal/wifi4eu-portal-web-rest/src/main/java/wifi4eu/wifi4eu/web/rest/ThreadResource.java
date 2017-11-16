package wifi4eu.wifi4eu.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wifi4eu.wifi4eu.common.dto.model.ThreadDTO;
import wifi4eu.wifi4eu.common.dto.rest.ErrorDTO;
import wifi4eu.wifi4eu.common.dto.rest.ResponseDTO;
import wifi4eu.wifi4eu.service.thread.ThreadService;

import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@Api(value = "/thread", description = "Thread object REST API services")
@RequestMapping("thread")
public class ThreadResource {
    @Autowired
    private ThreadService threadService;

    Logger _log = LoggerFactory.getLogger(CallResource.class);

    @ApiOperation(value = "Get all the thread entries")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<ThreadDTO> allThreads() {
        return threadService.getAllThreads();
    }

    @ApiOperation(value = "Get thread by specific id")
    @RequestMapping(value = "/{threadId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ThreadDTO getThreadById(@PathVariable("threadId") final Integer threadId) {
        _log.info("getThreadById: " + threadId);
        return threadService.getThreadById(threadId);
    }

    @ApiOperation(value = "Create thread")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseDTO createThread(@RequestBody final ThreadDTO threadDTO) {
        try {
            _log.info("createThread");
            ThreadDTO resThread = threadService.createThread(threadDTO);
            return new ResponseDTO(true, resThread, null);
        } catch (Exception e) {
            ErrorDTO errorDTO = new ErrorDTO(0, e.getMessage());
            return new ResponseDTO(false, null, errorDTO);
        }
    }


    @ApiOperation(value = "Delete thread by specific id")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseDTO deleteThread(@RequestBody final Integer threadId) {
        try {
            _log.info("deleteThread: " + threadId);
            ThreadDTO resThread = threadService.deleteThread(threadId);
            return new ResponseDTO(true, resThread, null);
        } catch (Exception e) {
            ErrorDTO errorDTO = new ErrorDTO(0, e.getMessage());
            return new ResponseDTO(false, null, errorDTO);
        }
    }

    @ApiOperation(value = "Get thread by specific lau id")
    @RequestMapping(value = "/lau/{lauId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ThreadDTO getThreadByLauId(@PathVariable("lauId") final Integer lauId) {
        _log.info("getThreadByLauId: " + lauId);
        return threadService.getThreadByLauId(lauId);
    }
}