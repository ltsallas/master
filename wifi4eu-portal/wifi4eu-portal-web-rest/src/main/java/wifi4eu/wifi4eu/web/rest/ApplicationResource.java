package wifi4eu.wifi4eu.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wifi4eu.wifi4eu.common.dto.model.ApplicationDTO;
import wifi4eu.wifi4eu.common.dto.rest.ErrorDTO;
import wifi4eu.wifi4eu.common.dto.rest.ResponseDTO;
import wifi4eu.wifi4eu.service.application.ApplicationService;

import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@Api(value = "/application", description = "Application object REST API services")
@RequestMapping("application")
public class ApplicationResource {
    @Autowired
    ApplicationService applicationService;

    Logger _log = LoggerFactory.getLogger(ApplicationResource.class);

    @ApiOperation(value = "Get all the applications")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<ApplicationDTO> allApplications() {
        _log.info("allApplications");
        return applicationService.getAllApplications();
    }

    @ApiOperation(value = "Get application by specific id")
    @RequestMapping(value = "/{applicationId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApplicationDTO getApplicationById(@PathVariable("applicationId") final Integer applicationId) {
        _log.info("getApplicationById: " + applicationId);
        return applicationService.getApplicationById(applicationId);
    }

    @ApiOperation(value = "Create application")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseDTO createApplication(@RequestBody final ApplicationDTO applicationDTO) {
        try {
            _log.info("createApplication");
            ApplicationDTO resApplication = applicationService.createApplication(applicationDTO);
            return new ResponseDTO(true, resApplication, null);
        } catch (Exception e) {
            ErrorDTO errorDTO = new ErrorDTO(0, e.getMessage());
            return new ResponseDTO(false, null, errorDTO);
        }
    }

    @ApiOperation(value = "Delete application by specific id")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseDTO deleteApplication(@RequestBody final Integer applicationId) {
        try {
            _log.info("deleteApplication: " + applicationId);
            ApplicationDTO resApplication = applicationService.deleteApplication(applicationId);
            return new ResponseDTO(true, resApplication, null);
        } catch (Exception e) {
            ErrorDTO errorDTO = new ErrorDTO(0, e.getMessage());
            return new ResponseDTO(false, null, errorDTO);
        }
    }

    @ApiOperation(value = "Get applications by specific supplier id")
    @RequestMapping(value = "/supplier/{supplierId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<ApplicationDTO> getApplicationsBySupplierId(@PathVariable("supplierId") final Integer supplierId) {
        _log.info("getApplicationsBySupplierId");
        return applicationService.getApplicationsBySupplierId(supplierId);
    }
}