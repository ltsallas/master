package wifi4eu.wifi4eu.abac.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import wifi4eu.wifi4eu.abac.data.Constants;
import wifi4eu.wifi4eu.abac.rest.vo.UserDetailsVO;
import wifi4eu.wifi4eu.abac.service.ECASUserService;

@RestController
@RequestMapping(path = "user")
public class UserController {

	@Autowired
	private ECASUserService ecasUserService;
		
	@PreAuthorize("hasRole('ROLE_INEA_OFFICER')")
	@RequestMapping(value = "details", method = RequestMethod.GET, produces = Constants.MIME_TYPE_REST_RESPONE)
	public UserDetailsVO details() throws IOException {
		return ecasUserService.getCurrentUserDetails();
	}
}
