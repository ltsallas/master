package wifi4eu.wifi4eu.repository.application;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import wifi4eu.wifi4eu.entity.application.ApplicantListItem;

import java.util.List;

public interface ApplicantListItemRepository extends CrudRepository<ApplicantListItem, Integer> {
    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY lauId ASC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByLauIdAsc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY lauId DESC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByLauIdDesc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY lauId ASC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByLauIdAsc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY lauId DESC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByLauIdDesc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY name ASC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByNameAsc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY name DESC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByNameDesc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY name ASC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByNameAsc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY name DESC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByNameDesc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY countryCode ASC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByCountryCodeAsc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY countryCode DESC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByCountryCodeDesc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY countryCode ASC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByCountryCodeAsc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY countryCode DESC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByCountryCodeDesc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY counter ASC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByCounterAsc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY counter DESC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByCounterDesc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY counter ASC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByCounterAsc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY counter DESC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByCounterDesc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY status ASC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByStatusAsc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY status DESC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByStatusDesc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY status ASC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByStatusAsc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY status DESC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByStatusDesc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY mediation ASC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByMediationAsc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL ORDER BY mediation DESC OFFSET ?#{[0]} ROWS FETCH NEXT ?#{[1]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListOrderByMediationDesc(Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY mediation ASC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByMediationAsc(String name, Integer offset, Integer count);

    @Query(value = "SELECT DISTINCT(m1.name) as name, l.id as lauId, l.country_code as countryCode, (SELECT rowNum FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY id) as rowNum FROM laus WHERE country_code = l.country_code) as lauOrder WHERE id = l.id) as countryRanking, (SELECT COUNT(1) FROM applications) as counter, (SELECT CASE WHEN sum(th.mediation) > 0 THEN 1 ELSE 0 END FROM threads th WHERE th.reason = m1.lau AND th.type = 1) AS mediation FROM municipalities m1 INNER JOIN registrations reg ON reg.municipality = m1.id INNER JOIN applications app ON app.registration = reg.id INNER JOIN laus l ON l.id = m1.lau WHERE l.country_code IS NOT NULL AND LOWER(name) LIKE LOWER(CONCAT('%',?#{[0]},'%')) ORDER BY mediation DESC OFFSET ?#{[1]} ROWS FETCH NEXT ?#{[2]} ROWS ONLY", nativeQuery = true)
    List<ApplicantListItem> findDgconnApplicantsListContainingNameOrderByMediationDesc(String name, Integer offset, Integer count);
}