package wifi4eu.wifi4eu.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wifi4eu.wifi4eu.common.dto.model.NutsDTO;
import wifi4eu.wifi4eu.common.ecas.UserHolder;
import wifi4eu.wifi4eu.common.security.UserContext;
import wifi4eu.wifi4eu.service.location.NutsService;
import wifi4eu.wifi4eu.service.user.UserService;

import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@Api(value = "/nuts", description = "Nuts object REST API services")
@RequestMapping("nuts")
public class NutsResource {
    @Autowired
    NutsService nutsService;

    Logger _log = LogManager.getLogger(NutsResource.class);

    @Autowired
    UserService userService;

    @ApiOperation(value = "Get all nuts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-API", value = "public", required = false, allowMultiple = false, dataType = "string", paramType = "header")
    })
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<NutsDTO> allNuts() {
        UserContext user = UserHolder.getUser();
        if (user == null || userService.getUserByUserContext(user) == null) {
            throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        return nutsService.getAllNuts();
    }

    @ApiOperation(value = "get nuts by specific id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-API", value = "public", required = false, allowMultiple = false, dataType = "string", paramType = "header")
    })
    @RequestMapping(value = "/{nutsId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public NutsDTO getNutsById(@PathVariable("nutsId") final Integer nutsId) {
        UserContext user = UserHolder.getUser();
        if (user == null || userService.getUserByUserContext(user) == null) {
            throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        return nutsService.getNutsById(nutsId);
    }

    @ApiOperation(value = "get nuts by code")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-API", value = "public", required = false, allowMultiple = false, dataType = "string", paramType = "header")
    })
    @RequestMapping(value = "/code/{code}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public NutsDTO getNutsByCode(@PathVariable("code") final String code) {
        UserContext user = UserHolder.getUser();
        if (user == null || userService.getUserByUserContext(user) == null) {
            throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        return nutsService.getNutsByCode(code);
    }

    @ApiOperation(value = "Get all nuts from a specific level")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-API", value = "public", required = false, allowMultiple = false, dataType = "string", paramType = "header")
    })
    @RequestMapping(value = "/level/{level}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<NutsDTO> getNutsByLevel(@PathVariable("level") final Integer level) {
        UserContext user = UserHolder.getUser();
        if (user == null || userService.getUserByUserContext(user) == null) {
            throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        return nutsService.getNutsByLevel(level);
    }

    @ApiOperation(value = "Get all nuts from a specific countryCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-API", value = "public", required = false, allowMultiple = false, dataType = "string", paramType = "header")
    })
    @RequestMapping(value = "/countryCode/{countryCode}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<NutsDTO> getNutsByCountryCode(@PathVariable("countryCode") final String countryCode) {
        UserContext user = UserHolder.getUser();
        if (user == null || userService.getUserByUserContext(user) == null) {
            throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        return nutsService.getNutsByCountryCode(countryCode);
    }

    @ApiOperation(value = "Get all nuts from a specific countryCode and level order by Label Asc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-API", value = "public", required = false, allowMultiple = false, dataType = "string", paramType = "header")
    })
    @RequestMapping(value = "/countryCode/{countryCode}/level/{level}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<NutsDTO> getNutsByCountryCodeAndLevelOrderByLabelAsc(@PathVariable("countryCode") final String countryCode, @PathVariable("level") final Integer level) {
        UserContext user = UserHolder.getUser();
        if (user == null || userService.getUserByUserContext(user) == null) {
            throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        return nutsService.getNutsByCountryCodeAndLevelOrderByLabelAsc(countryCode, level);
    }
}