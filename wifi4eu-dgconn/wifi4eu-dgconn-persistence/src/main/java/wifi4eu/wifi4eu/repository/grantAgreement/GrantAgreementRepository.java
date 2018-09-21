package wifi4eu.wifi4eu.repository.grantAgreement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import wifi4eu.wifi4eu.entity.grantAgreement.GrantAgreement;

public interface GrantAgreementRepository extends CrudRepository<GrantAgreement, Integer> {

    Integer countByApplicationId(int applicationId);

    List<GrantAgreement> findByApplicationId(Integer applicationId);

    //TODO: it is not enough to look by municipalityId as 1 municipality can have several grant agreements even with the same date
    GrantAgreement findByApplicationRegistrationMunicipalityIdAndDateCounterSignatureIsNull(Integer municipalityId);

}
