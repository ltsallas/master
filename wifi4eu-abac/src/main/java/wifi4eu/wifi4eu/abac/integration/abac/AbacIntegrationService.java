package wifi4eu.wifi4eu.abac.integration.abac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.abac.data.entity.LegalCommitment;
import wifi4eu.wifi4eu.abac.data.entity.LegalEntity;
import wifi4eu.wifi4eu.abac.data.enums.AbacWorkflowStatus;
import wifi4eu.wifi4eu.abac.data.enums.LegalCommitmentWorkflowStatus;
import wifi4eu.wifi4eu.abac.data.repository.BudgetaryCommitmentRepository;
import wifi4eu.wifi4eu.abac.data.repository.LegalCommitmentRepository;
import wifi4eu.wifi4eu.abac.data.repository.LegalEntityRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AbacIntegrationService {

    private static int FIRST_PAGE = 0;

    private final Logger log = LoggerFactory.getLogger(AbacIntegrationService.class);

    @Autowired
    LegalEntityRepository legalEntityRepository;

    @Autowired
    BudgetaryCommitmentRepository budgetaryCommitmentRepository;

    @Autowired
    LegalCommitmentRepository legalCommitmentRepository;

    /**
     * Send the legal entities with status READY_FOR_ABAC, limited to a maximum of @maxRecords
     * @param maxRecords
     */
    public void findAndSendLegalEntitiesReadyToABAC(Integer maxRecords) {
        Pageable pageable = PageRequest.of(FIRST_PAGE, maxRecords);
        List<LegalEntity> legalEntities = legalEntityRepository.findByWfStatusOrderByDateCreated(AbacWorkflowStatus.READY_FOR_ABAC, pageable);

        if (!legalEntities.isEmpty()) {
            log.info("Found {} legal entities ready to be sent to ABAC...", legalEntities.size());
        }

        try {
            for (LegalEntity legalEntity : legalEntities) {
                createLegalEntityInAbac(legalEntity);
            }
        } catch (Exception e){
            log.error("Error sending data to abac: {}", e.getMessage());
        }

    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void createLegalEntityInAbac(LegalEntity legalEntity) {
        try {
            log.info("Insert legal entity {} into abac", legalEntity.getId());
            legalEntityRepository.createFinancialLegalEntity(legalEntity.getId());
        } catch (Exception e){
            log.error("Error sending data to abac: {}", e.getMessage());
        }
    }

    public void updateLegalEntitiesStatuses() {
        try {
            legalEntityRepository.updateFinancialLegalEntitiesStatuses();
        } catch (Exception e){
            log.error("Error retrieving data from abac: {}", e.getMessage());
        }
    }

    public void findAndSendBudgetaryCommitmentsReadyToABAC(Integer maxRecords) {
        Pageable pageable = PageRequest.of(FIRST_PAGE, maxRecords);
        List<LegalEntity> legalEntities = legalEntityRepository.findAvailableLegalEntitiesForBudgetaryCommitmentCreation(pageable);

        if (!legalEntities.isEmpty()) {
            log.info("Found {} legal entities with budgetary commitments to be created in ABAC...", legalEntities.size());
        }

        try {
            for (LegalEntity legalEntity : legalEntities) {
                log.info("Insert BC for legal entity id {} in ABAC", legalEntity.getId());
                budgetaryCommitmentRepository.createBudgetaryCommitmentInAbac(legalEntity.getId());
            }
        } catch (Exception e){
            log.error("Error sending data to abac: {}", e.getMessage());
        }
    }

    public void updateBudgetaryCommitmentStatuses() {
        try {
            budgetaryCommitmentRepository.updateBudgetaryCommitmentStatuses();
        } catch (Exception e){
            log.error("Error retrieving data from abac: {}", e.getMessage());
        }
    }

	public void updateLegalCommitmentStatuses() {
        try {
            legalCommitmentRepository.updateLegalCommitmentStatuses();
        } catch (Exception e){
            log.error("Error retrieving data from abac: {}", e.getMessage());
        }
	}

    public void findAndSendLegalCommitmentsReadyToABAC(Integer maxRecords) {
        Pageable pageable = PageRequest.of(FIRST_PAGE, maxRecords);
        List<LegalCommitment> legalCommitments = legalCommitmentRepository.findLegalCommitmentsAvailableForCreation(pageable);

        for (LegalCommitment legalCommitment : legalCommitments) {
            legalCommitmentRepository.createLegalCommitmentInABAC(legalCommitment.getId());
        }
    }
}
