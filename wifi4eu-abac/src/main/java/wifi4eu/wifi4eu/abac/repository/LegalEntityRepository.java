package wifi4eu.wifi4eu.abac.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wifi4eu.wifi4eu.abac.entity.LegalEntity;
import wifi4eu.wifi4eu.abac.entity.MonitoringRow;
import wifi4eu.wifi4eu.abac.service.AbacWorkflowStatusEnum;

import java.util.List;
import java.util.Set;

public interface LegalEntityRepository extends CrudRepository<LegalEntity, Integer> {

	LegalEntity findByMid(Integer mid);

	LegalEntity findByOfficialName(String officialName);

	@Query(value = "SELECT le FROM LegalEntity le WHERE le.abacFelId is not null")
	List<LegalEntity> findLegalEntitiesProcessedInAbac();

	List<LegalEntity> findByWfStatusOrderByDateCreated(AbacWorkflowStatusEnum status, Pageable pageable);

	@Procedure(name = "CREATE_LEF_IN_ABAC")
	void createFinancialLegalEntity(@Param("LEGALENTITYID") Long legalEntityID);

	@Query(value = "SELECT new wifi4eu.wifi4eu.abac.entity.MonitoringRow(le.id, le.city, le.countryCode, le.wfStatus) FROM LegalEntity le")
	List<MonitoringRow> findMonitoringData();
}
