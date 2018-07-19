package wifi4eu.wifi4eu.abac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import wifi4eu.wifi4eu.abac.entity.BudgetaryCommitment;
import wifi4eu.wifi4eu.abac.entity.LegalEntity;

public interface BudgetaryCommitmentRepository extends CrudRepository<BudgetaryCommitment, Integer> {

	@Query(value = "SELECT bc FROM BudgetaryCommitment bc WHERE bc.wfStatus in ('ABAC_FINISH', 'ABAC_ERROR')")
	List<BudgetaryCommitment> findBCFinishedInAbac();

}
