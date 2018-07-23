package wifi4eu.wifi4eu.abac.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wifi4eu.wifi4eu.abac.entity.LegalEntity;
import wifi4eu.wifi4eu.abac.repository.LegalEntityRepository;
import wifi4eu.wifi4eu.abac.utils.ParserCSV2Entity;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class LegalEntityService {

	private static int FIRST_PAGE = 0;

	private final Logger log = LoggerFactory.getLogger(LegalEntityService.class);
	private LegalEntityRepository legalEntityrepository;

	@Autowired
	private AbacIntegrationService abacIntegrationService;

	@Autowired
	public LegalEntityService(LegalEntityRepository legalEntityrepository) {
		this.legalEntityrepository = legalEntityrepository;
	}

	// @ Transactional
	public void importLegalEntityFile(MultipartFile file) throws IOException {
		log.info("importLegalEntityFile");

		byte[] contentBytes = file.getBytes();

		log.info("recovering list of items");
		ParserCSV2Entity<LegalEntity> parser = new ParserCSV2Entity();
		List<LegalEntity> items = parser.parseCSV2Entity(contentBytes, LegalEntity.class);

		log.info("printing list of items");
		for (LegalEntity legalEntity : items) {
			log.info("Item recovered: " + legalEntity.toString());
			legalEntityrepository.save(legalEntity);
		}

		log.info("storing list of items");
	}

	// @ Transactional
	public void importLegalEntityContent(String content) throws IOException {
		log.info("importLegalEntityContent");

		byte[] contentBytes = content.getBytes();

		log.info("recovering list of items");
		ParserCSV2Entity<LegalEntity> parser = new ParserCSV2Entity();
		List<LegalEntity> items = parser.parseCSV2Entity(contentBytes, LegalEntity.class);

		log.info("printing list of items");
		for (LegalEntity legalEntity : items) {
			log.info("Item recovered: " + legalEntity.toString());
			// Override status
			legalEntity.setWfStatus(AbacWorkflowStatusEnum.IMPORTED.getValue());
			legalEntityrepository.save(legalEntity);
		}

		log.info("storing list of items");
	}

	public String showLegalEntityFile() {
		log.info("showLegalEntityFile");

		log.info("recovering list of items");
		Iterable<LegalEntity> list = legalEntityrepository.findAll();

		log.info("parsing list of items");
		StringBuilder data = new StringBuilder();

		boolean first = true;
		data.append("<table style=\"width:100%\" border=\"1\">");
		data.append("<tr>");

		// headers
		String headers[] = new String[] { "id", "mid", "officialName", "region", "languageCode", "countryCode",
				"officialAddress", "officialAddressStrNo", "postalCode", "abacFelId", "wfStatus", "dateCreated" };
		for (String header : headers) {
			data.append("<th>").append(header).append("</th>");
		}
		data.append("\r\n");

		// data
		for (LegalEntity legalEntity : list) {
			data.append("</tr><tr>\r\n");
			data.append("<td>").append(legalEntity.getId()).append("</td>");
			data.append("<td>").append(legalEntity.getMid()).append("</td>");
			data.append("<td>").append(legalEntity.getOfficialName()).append("</td>");
			data.append("<td>").append(legalEntity.getRegion()).append("</td>");
			data.append("<td>").append(legalEntity.getLanguageCode()).append("</td>");
			data.append("<td>").append(legalEntity.getCountryCode()).append("</td>");
			data.append("<td>").append(legalEntity.getOfficialAddress()).append("</td>");
			data.append("<td>").append(legalEntity.getOfficialAddressStrNo()).append("</td>");
			data.append("<td>").append(legalEntity.getPostalCode()).append("</td>");
			data.append("<td>").append(legalEntity.getAbacFelId()).append("</td>");
			data.append("<td>").append(legalEntity.getWfStatus()).append("</td>");
			data.append("<td>").append(legalEntity.getDateCreated()).append("</td>");
		}
		data.append("</tr>");
		data.append("</table>");

		return data.toString();
	}

	public String exportLegalEntityFile() {
		log.info("exportLegalEntityFile");

		log.info("recovering list of items");
		List<LegalEntity> list = legalEntityrepository.findLegalEntitiesProcessedInAbac();

		log.info("parsing list of items");
		String data = parseLegalEntity2String(list);
		// log.info("data: " + data);

		return data;
	}

	/**
	 * Send the legal entities with status READY_FOR_ABAC, limited to a maximum of @maxRecords
	 * @param maxRecords
	 */
	public void findAndSendLegalEntitiesReadyToABAC(Integer maxRecords) {
		Pageable pageable = PageRequest.of(FIRST_PAGE, maxRecords);
		List<LegalEntity> legalEntities = legalEntityrepository.findByWfStatusOrderByDateCreated(AbacWorkflowStatusEnum.READY_FOR_ABAC.toString(), pageable);

		if (!legalEntities.isEmpty()) {
			log.info(String.format("Found %s legal entities ready to be sent to ABAC...", legalEntities.size()));
		}

		for (LegalEntity legalEntity: legalEntities) {
			sendLegalEntityToABAC(legalEntity);
		}
	}

	@Transactional
	private void sendLegalEntityToABAC(LegalEntity legalEntity) {
		//Requet the creation of the legal entity in ABAC
		abacIntegrationService.createLegalEntity(legalEntity);

		//Update the legal entity status in Wifi4EU DB
		legalEntity.setWfStatus(AbacWorkflowStatusEnum.WAITING_FOR_ABAC.toString());
		legalEntityrepository.save(legalEntity);
	}

	public void checkLegalEntityCreationStatus(Integer maxRecords) {
		Pageable pageable = PageRequest.of(FIRST_PAGE, maxRecords);
		List<LegalEntity> legalEntities = legalEntityrepository.findByWfStatusOrderByDateCreated(AbacWorkflowStatusEnum.WAITING_FOR_ABAC.toString(), pageable);

		if (!legalEntities.isEmpty()) {
			log.info(String.format("Found %s legal entities waiting for ABAC to create them", legalEntities.size()));
		}

		for(LegalEntity legalEntity : legalEntities) {
			String status = abacIntegrationService.checkLegalEntityCreationStatus(legalEntity);
			legalEntity.setWfStatus(status);
		}
	}

	/**
	 * NOTE: temporal workaround, should be defined in a generic parser or a mapper
	 */
	private String parseLegalEntity2String(List<LegalEntity> list) {
		String separator = "|";
		String headers[] = new String[] { "id", "mid", "officialName", "abacFelId", "wfStatus" };
		StringBuilder data = new StringBuilder();

		// headers
		boolean first = true;
		for (String header : headers) {
			if (first) {
				data.append(header);
				first = false;
			} else {
				data.append(separator).append(header);
			}
		}
		data.append("\r\n");

		// data
		if (list != null && !list.isEmpty()) {
			for (LegalEntity legalEntity : list) {
				data.append(legalEntity.getId()).append(separator).append(legalEntity.getMid()).append(separator)
						.append(legalEntity.getOfficialName()).append(separator).append(legalEntity.getAbacFelId())
						.append(separator).append(legalEntity.getWfStatus()).append("\r\n");
			}
		}
		data.append("\r\n");

		return data.toString();
	}

}
