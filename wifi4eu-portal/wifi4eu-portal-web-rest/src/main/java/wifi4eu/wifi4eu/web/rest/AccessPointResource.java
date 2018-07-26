package wifi4eu.wifi4eu.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wifi4eu.wifi4eu.common.dto.rest.ResponseDTO;
import wifi4eu.wifi4eu.entity.access_point.AccessPoint;
import wifi4eu.wifi4eu.service.access_point.AccessPointService;
import wifi4eu.wifi4eu.util.AzureTest;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "*")
@Controller
@Api(value = "/access-points", description = "Beneficiary object REST API services")
@RequestMapping("access-points")
public class AccessPointResource {

    @Autowired
    AccessPointService accessPointService;

    @Autowired
    AzureTest azureTest;


    @ApiOperation(value = "Get supplier by specific user id")
    @RequestMapping(value = "/azure", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseDTO testingIt() throws IOException {
        azureTest.testingIt();
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setData("");
        return responseDTO;
    }

    @ApiOperation(value = "Get all Access Points per installation site ID")
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseDTO getAccessPointPerInstallationSite(@RequestBody final Map<String, Object> map) {
        return accessPointService.findAccessPointsPerInstallationSite(map);
    }


    @ApiOperation(value = "Get Access point by ID")
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseDTO getAccessPointById(@PathVariable("id") final int id) {
        return accessPointService.getAccessPointById(id);
    }

    @ApiOperation(value = "Edit Access Point")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseDTO editBeneficiaryDisplayedListDTO(@RequestBody AccessPoint request) {
        ResponseDTO response = new ResponseDTO();
        response.setSuccess(false);
        response.setData("Not Implemented");
        return response;
    }

}
