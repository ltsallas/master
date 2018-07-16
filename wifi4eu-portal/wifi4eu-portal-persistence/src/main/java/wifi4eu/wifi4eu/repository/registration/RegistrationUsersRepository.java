package wifi4eu.wifi4eu.repository.registration;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import wifi4eu.wifi4eu.entity.registration.RegistrationUsers;

import java.util.List;

public interface RegistrationUsersRepository extends CrudRepository<RegistrationUsers, Integer> {

    RegistrationUsers findByUserIdAndRegistrationId(Integer userId, Integer registrationId);

    List<RegistrationUsers> findByContactEmail(String email);

    @Query(value = "select ru.* from registration_users ru inner join registrations r on ru.registration = r.id inner join municipalities m on r.municipality = m.id where ru.contact_email = ?#{[0]} and m.id = ?#{[1]}", nativeQuery = true)
    RegistrationUsers findByContactEmailAndMunicipality(String email, Integer municipalityId);
}
