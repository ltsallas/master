package wifi4eu.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wifi4eu.model.Language;

public interface LanguageRepository extends CrudRepository<Language, Long> {
	public List<Language> findByOrderByName();
	public Language findByCodeIgnoreCase(String code);
}
