package wifi4eu.supplier.web.rest.installation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wifi4eu.wifi4eu.common.dto.rest.ResponseDTO;
import wifi4eu.wifi4eu.entity.installation.InstallationSite;
import wifi4eu.wifi4eu.service.installation.InstallationSiteService;
import wifi4eu.wifi4eu.service.security.PermissionChecker;

import java.util.Map;


@CrossOrigin(origins = "*")
@Controller
@Api(value = "/installation-site", description = "Installation Site object REST API services")
@RequestMapping("installation-site")
public class InstallationSiteResource {

    @Autowired
    private InstallationSiteService installationSiteService;

    @Autowired
    PermissionChecker permissionChecker;

    private final Logger _log = LoggerFactory.getLogger(InstallationSiteResource.class);


    @ApiOperation(value="Get all installation sites by beneficiary")
    @RequestMapping(value="/list", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseDTO getInstallationSiteListByBeneficiary(@RequestBody final Map<String, Object> installationText){
        try {
            permissionChecker.checkSupplierPermission();
        } catch (Exception e) {
            return permissionChecker.getAccessDeniedResponse();
        }
        return installationSiteService.findInstallationSitesByBeneficiariesOrdered(installationText);
    }

    @ApiOperation(value = "Edit Installation Site")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseDTO editInstallationSiteDTO(@RequestBody InstallationSite request) {
        ResponseDTO response = new ResponseDTO();
        response.setSuccess(false);
        response.setData("Not Implemented");
        return response;
    }

    @ApiOperation(value = "Add a new installation site")
    @RequestMapping(value= "/add", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseDTO addInstallationSite(@RequestBody final Map<String, Object> installationText){
        try {
            permissionChecker.checkSupplierPermission();
        } catch (Exception e) {
            return permissionChecker.getAccessDeniedResponse();
        }
        return installationSiteService.addAndUpdateInstallationSite(installationText);
    }

    @ApiOperation(value = "Update an installation site")
    @RequestMapping(value= "/update", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseDTO updateInstallationSite(@RequestBody final Map<String, Object> installationText){
        try {
            permissionChecker.checkSupplierPermission();
        } catch (Exception e) {
            return permissionChecker.getAccessDeniedResponse();
        }
        return installationSiteService.addAndUpdateInstallationSite(installationText);
    }

    @ApiOperation(value = "Get installation site details")
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDTO getInstallationSite(@PathVariable("id") int id){
        try {
            permissionChecker.checkSupplierPermission();
        } catch (Exception e) {
            return permissionChecker.getAccessDeniedResponse();
        }
        return installationSiteService.getInstallationReport(id);
    }

    @ApiOperation(value = "Remove an installation site")
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDTO removeInstallSite(@PathVariable("id") int id){
        try {
            permissionChecker.checkSupplierPermission();
        } catch (Exception e) {
            return permissionChecker.getAccessDeniedResponse();
        }
        return installationSiteService.removeInstallationReport(id);
    }

}
