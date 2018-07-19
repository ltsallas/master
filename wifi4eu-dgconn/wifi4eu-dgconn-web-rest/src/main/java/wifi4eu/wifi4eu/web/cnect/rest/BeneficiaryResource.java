package wifi4eu.wifi4eu.web.cnect.rest;

import com.google.common.net.InetAddresses;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wifi4eu.wifi4eu.common.dto.model.BeneficiaryDTO;
import wifi4eu.wifi4eu.common.dto.model.BeneficiaryListItemDTO;
import wifi4eu.wifi4eu.common.dto.model.RegistrationDTO;
import wifi4eu.wifi4eu.common.dto.model.UserDTO;
import wifi4eu.wifi4eu.common.dto.rest.ErrorDTO;
import wifi4eu.wifi4eu.common.dto.rest.ResponseDTO;
import wifi4eu.wifi4eu.common.ecas.UserHolder;
import wifi4eu.wifi4eu.common.exception.AppException;
import wifi4eu.wifi4eu.common.security.UserContext;
import wifi4eu.wifi4eu.common.utils.RequestIpRetriever;
import wifi4eu.wifi4eu.service.beneficiary.BeneficiaryService;
import wifi4eu.wifi4eu.service.security.PermissionChecker;
import wifi4eu.wifi4eu.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@Api(value = "/beneficiary", description = "Beneficiary object REST API services")
@RequestMapping("beneficiary")
public class BeneficiaryResource {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private PermissionChecker permissionChecker;

    @Autowired
    private UserService userService;

    private final Logger _log = LogManager.getLogger(BeneficiaryResource.class);

    @ApiOperation(value = "getBeneficiaryListItem")
    @RequestMapping(value = "/getBeneficiaryListItem", method = RequestMethod.GET)
    @ResponseBody
    public BeneficiaryListItemDTO getBeneficiaryListItem() {
        return new BeneficiaryListItemDTO();
    }

    @ApiOperation(value = "findDgconnBeneficiaresList")
    @RequestMapping(value = "/findDgconnBeneficiaresList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO findDgconnBeneficiaresList(@RequestParam("offset") final Integer offset, @RequestParam("count") final Integer count, @RequestParam("orderField") String orderField, @RequestParam("orderType") Integer orderType, HttpServletResponse response) throws IOException {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Getting DGConn beneficiaries by offset " + offset + ", count" + count + ", order field" + orderField + " and order type " + orderType);
        try {
            if (!permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            ResponseDTO res = new ResponseDTO(true, null, null);
            res.setData(beneficiaryService.findDgconnBeneficiaresList(null, offset, count, orderField, orderType));
            res.setXTotalCount(beneficiaryService.getCountDistinctMunicipalities());
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + "- DGConn Beneficiaries retrieved successfully");
            return res;
        } catch (AccessDeniedException ade) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- You have no permissions to retrieve DGConn beneficiaries", ade.getMessage());
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        } catch (Exception e) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- DGConn Beneficiaries cannot been retrieved", e);
            return new ResponseDTO(false, null, new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        }
    }

    @ApiOperation(value = "findDgconnBeneficiaresListSearchingName")
    @RequestMapping(value = "/findDgconnBeneficiaresListSearchingName", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO findDgconnBeneficiaresListSearchingName(@RequestParam("name") final String name, @RequestParam("offset") final Integer offset, @RequestParam("count") final Integer count, @RequestParam("orderField") String orderField, @RequestParam("orderType") Integer orderType, HttpServletResponse response) throws IOException {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Getting DGConn beneficiaries by searching name " + name + ",offset " + offset + ", count" + count + ", order field" + orderField + " and order type " + orderType);
        try {
            if (!permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            ResponseDTO res = new ResponseDTO(true, null, null);
            res.setData(beneficiaryService.findDgconnBeneficiaresList(name, offset, count, orderField, orderType));
            res.setXTotalCount(beneficiaryService.getCountDistinctMunicipalitiesContainingName(name));
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + "- DGConn Beneficiaries retrieved successfully");
            return res;
        } catch (AccessDeniedException ade) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- You have no permissions to retrieve DGConn beneficiaries", ade.getMessage());
            response.sendError(HttpStatus.NOT_FOUND.value());
            return new ResponseDTO(false, null, new ErrorDTO(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase()));
        } catch (Exception e) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- DGConn Beneficiaries cannot been retrieved", e);
            return new ResponseDTO(false, null, new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        }
    }

    @ApiOperation(value = "exportCSVDGConnBeneficiariesList")
    @RequestMapping(value = "/exportCSVDGConnBeneficiariesList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO exportCSVDGConnBeneficiariesList(HttpServletResponse response) throws IOException {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Exporting CSV from DGConn beneficiaries");
        try {
            if (!permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            ResponseDTO res = new ResponseDTO(true, null, null);
            res.setData(beneficiaryService.exportCSVDGConnBeneficiariesList());
            res.setXTotalCount(beneficiaryService.getCountDistinctMunicipalities());
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + "- CSV exported successfully");
            return res;
        } catch (AccessDeniedException ade) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- You have no permissions to export CSV", ade.getMessage());
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }

    @ApiOperation(value = "exportCSVDGConnBeneficiariesListSearchingName")
    @RequestMapping(value = "/exportCSVDGConnBeneficiariesListSearchingName", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO exportCSVDGConnBeneficiariesListSearchingName(@RequestParam("name") final String name, HttpServletResponse response) throws IOException {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Exporting CSV from DGConn beneficiaries by searching name " + name);
        try {
            if (!permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            ResponseDTO res = new ResponseDTO(true, null, null);
            res.setData(beneficiaryService.exportCSVDGConnBeneficiariesListSearchingName(name));
            res.setXTotalCount(beneficiaryService.getCountDistinctMunicipalitiesContainingName(name));
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + "- CSV exported successfully");
            return res;
        } catch (AccessDeniedException ade) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- You have no permissions to export CSV", ade.getMessage());
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }

    @ApiOperation(value = "exportExcelDGConnBeneficiariesList")
    @RequestMapping(value = "/exportExcelDGConnBeneficiariesList", method = RequestMethod.POST, headers = "Accept=application/vnd.ms-excel", produces = "application/vnd.ms-excel")
    @ResponseBody
    public ResponseEntity<byte[]> exportExcelDGConnBeneficiariesList(HttpServletResponse response) throws IOException {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Exporting Excel from DGConn beneficiaries");
        try {
            if (!permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            ResponseEntity<byte[]> responseReturn = null;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            String filename = "dgconn-beneficiaries.xls";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            responseReturn = new ResponseEntity<>(beneficiaryService.exportExcelDGConnBeneficiariesList(), headers, HttpStatus.OK);
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + "- Excel exported successfully");
            return responseReturn;
        } catch (AccessDeniedException ade) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- You have no permissions to export Excel", ade.getMessage());
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }

    @ApiOperation(value = "exportExcelDGConnBeneficiariesListSearchingName")
    @RequestMapping(value = "/exportExcelDGConnBeneficiariesListSearchingName", method = RequestMethod.POST, headers = "Accept=application/vnd.ms-excel", produces = "application/vnd.ms-excel")
    @ResponseBody
    public ResponseEntity<byte[]> exportExcelDGConnBeneficiariesListSearchingName(@RequestParam("name") final String name, HttpServletResponse response) throws IOException {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        _log.debug("ECAS Username: " + userConnected.getEcasUsername() + " - Exporting Excel from DGConn beneficiaries by searching name" + name);
        try {
            if (!permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            ResponseEntity<byte[]> responseReturn = null;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            String filename = "dgconn-beneficiaries.xls";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            responseReturn = new ResponseEntity<>(beneficiaryService.exportExcelDGConnBeneficiariesListContainingName(name), headers, HttpStatus.OK);
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + "- Excel exported successfully");
            return responseReturn;
        } catch (AccessDeniedException ade) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- You have no permissions to export Excel", ade.getMessage());
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }

    @ApiOperation(value = "Get beneficiaries from final list by call")
    @RequestMapping(value = "/finalBeneficiaries/call/{callId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDTO getBeneficiariesFromFinalList(@PathVariable("callId") final Integer callId,
                                                     @RequestParam("countryCode") final String countryCode,
                                                     @RequestParam("municipality") final String municipality,
                                                     @RequestParam("page") final Integer page,
                                                     @RequestParam("size") final Integer size,
                                                     @RequestParam("field") final String field,
                                                     @RequestParam("sortDirection") final String sortDirection,
                                                     HttpServletResponse response) throws IOException {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        try {
            if (!permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            ResponseDTO responseDTO = beneficiaryService.findBeneficiariesFromFinalList(callId, countryCode, municipality, page, size, field, sortDirection);
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + "- Beneficiaries from final list retrieved successfully");
            return responseDTO;
        } catch (AccessDeniedException ade) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- You have no permissions to retrieve beneficiaries from final list", ade.getMessage());
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        }

    }

    @ApiOperation(value = "Download PDF grant agreement by beneficiaryId")
    @RequestMapping(value = "exportBeneficiaryPdfGrantAgreement", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> exportBeneficiaryPdfGrantAgreement(@RequestParam("registrationId") final Integer registrationId, HttpServletResponse response) throws IOException {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        try {
            if (!permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            // ResponseDTO responseDTO = beneficiaryService.downloadGrantAgreementPdfFromRegistrationId(registrationId);
            // _log.info("ECAS Username: " + userConnected.getEcasUsername() + "- Download PDF Grant Agreement successfully");
            // return responseDTO;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String filename = "grantAgreementPdf.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ByteArrayOutputStream file = beneficiaryService.downloadGrantAgreementPdfFromRegistrationId(registrationId);
            return new ResponseEntity<>(file.toByteArray(), headers, HttpStatus.OK);
        } catch (AccessDeniedException ade) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- You have no permissions to download the PDF Grant Agreement", ade.getMessage());
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        }

    }

    @ApiOperation(value = "Cancel beneficiary by registration ID")
    @RequestMapping(value = "cancel/registration/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDTO cancelBeneficiaryFromRegistrationId(@PathVariable("registrationId") final Integer registrationId, @RequestParam("reason") final String reason, @RequestParam("callId") final Integer callId, HttpServletResponse response) throws IOException {
        UserContext userContext = UserHolder.getUser();
        UserDTO userConnected = userService.getUserByUserContext(userContext);
        try {
            if (!permissionChecker.checkIfDashboardUser()) {
                throw new AccessDeniedException(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
            ResponseDTO responseDTO = beneficiaryService.cancelBeneficiaryFromRegistrationId(registrationId,reason,callId);
            _log.info("ECAS Username: " + userConnected.getEcasUsername() + "- Beneficiary cancelled successfully");
            return responseDTO;
        } catch (AccessDeniedException ade) {
            _log.error("ECAS Username: " + userConnected.getEcasUsername() + "- You have no permissions to cancel this beneficiary", ade.getMessage());
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        }

    }




}