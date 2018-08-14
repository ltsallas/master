package wifi4eu.wifi4eu.abac.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wifi4eu.wifi4eu.abac.rest.vo.ResponseVO;
import wifi4eu.wifi4eu.abac.service.LegalCommitmentService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "legalCommitment")
public class LegalCommitmentController {

	private final Logger log = LoggerFactory.getLogger(LegalCommitmentController.class);

	private LegalCommitmentService legalCommitmentService;

	@Autowired
	public LegalCommitmentController(LegalCommitmentService legalCommitmentService) {
		this.legalCommitmentService = legalCommitmentService;
	}

	@RequestMapping(value = "import", method = RequestMethod.POST, produces = "application/json")
	public ResponseVO importLegalCommitment(@RequestBody String file) {
		log.info("importLegalCommitment");
		ResponseVO result = new ResponseVO();
		try {
			result.success("Imported OK!");
		}catch(Exception e) {
			result.error(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "export", method = RequestMethod.GET, produces = "application/zip")
	public ResponseEntity<byte[]> exportLegalCommitment(final HttpServletResponse response, Model model) throws Exception {
		log.info("exportLegalCommitment");
		return null;
	}

}