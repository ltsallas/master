package wifi4eu.wifi4eu.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import wifi4eu.wifi4eu.entity.user.UserContactDetails;

import java.util.List;

public interface UserContactDetailsRepository extends CrudRepository<UserContactDetails, Integer>{

    @Query(value = "select u.id as id, u.name as name, u.surname as surname, u.address as address, u.address_num as addressNumm, u.postal_code as postalCode, u.city as city, u.country as country, u.ecas_email as email, ru.main as main from users u inner join registration_users ru on ru._user = u.id where ru.registration = ?#{[0]}", nativeQuery = true)
    List<UserContactDetails> findUsersContactDetailsByRegistrationId(Integer registrationId);
}
