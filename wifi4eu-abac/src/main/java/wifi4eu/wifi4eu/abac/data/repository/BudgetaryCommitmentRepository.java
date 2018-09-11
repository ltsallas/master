package wifi4eu.wifi4eu.abac.data.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wifi4eu.wifi4eu.abac.data.entity.BudgetaryCommitment;
import wifi4eu.wifi4eu.abac.data.enums.AbacWorkflowStatus;

import java.util.List;

public interface BudgetaryCommitmentRepository extends CrudRepository<BudgetaryCommitment, Integer> {

	@Procedure(name = "CREATE_BC_IN_ABAC")
	void createBudgetaryCommitmentInAbac(@Param("LEGALENTITYID") Long legalEntityID);

	@Procedure(name = "UPDATE_BC_STATUS_FROM_ABAC")
	void updateBudgetaryCommitmentStatuses();

	BudgetaryCommitment findByLegalEntityMid(Long municipalityPortalId);

	Long countAllByWfStatusNotInAndBatchRefEquals(List<AbacWorkflowStatus> wfStatuses, String batchRef);

	List<BudgetaryCommitment> findAllByBatchRefEquals(String batchRef);
}