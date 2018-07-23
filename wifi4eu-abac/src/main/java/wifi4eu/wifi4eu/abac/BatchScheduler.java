package wifi4eu.wifi4eu.abac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import wifi4eu.wifi4eu.abac.service.LegalEntityService;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "batch.scheduler", name="enabled", havingValue="true", matchIfMissing = false)
public class BatchScheduler {

    @Value("${batch.legalentity.create.maxrecords}")
    private Integer MAX_RECORDS_CREATE_LEGAL_ENTITY;

    @Value("${batch.legalentity.checkstatus.maxrecords}")
    private Integer MAX_RECORDS_CHECK_CREATION_STATUS;

    @Autowired
    LegalEntityService legalEntityService;
	
    @Scheduled(cron = "${batch.legalentity.create.crontable}")
    public void createLegalEntitiesInABAC() {
        legalEntityService.findAndSendLegalEntitiesReadyToABAC(MAX_RECORDS_CREATE_LEGAL_ENTITY);
    }

    @Scheduled(cron = "${batch.legalentity.checkstatus.crontable}")
    public void checkLegalEntityCreationStatus() {
        legalEntityService.checkLegalEntityCreationStatus(MAX_RECORDS_CHECK_CREATION_STATUS);
    }
}
