package wifi4eu.wifi4eu.abac.utils.csvparser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import wifi4eu.wifi4eu.abac.data.dto.LegalEntityDocumentCSVRow;
import wifi4eu.wifi4eu.abac.data.entity.Document;
import wifi4eu.wifi4eu.abac.data.enums.DocumentCSVColumn;

@Component
public class LegalEntityDocumentCSVFileParser extends DocumentCSVFileParser{

	@SuppressWarnings("rawtypes")
	@Override
	public Enum[] getMandatoryFields() {
		Enum[] result = { DocumentCSVColumn.MUNICIPALITY_PORTAL_ID };  
		return result;
	}
	
	@Override
	protected List<LegalEntityDocumentCSVRow> mapRowsToEntities(CSVParser csvParser) {

		try {
			List<LegalEntityDocumentCSVRow> documents = new ArrayList<>();

			for (CSVRecord csvRecord : csvParser) {
				LegalEntityDocumentCSVRow documentCSVRow = new LegalEntityDocumentCSVRow();
				documentCSVRow.setMunicipalityPortalId(
					StringUtils.isEmpty(csvRecord.get(DocumentCSVColumn.MUNICIPALITY_PORTAL_ID)) ?  null : Long.parseLong(csvRecord.get(DocumentCSVColumn.MUNICIPALITY_PORTAL_ID))
				);
				mapCSVRowToDocument(documentCSVRow, csvRecord);
				documents.add(documentCSVRow);
			}
			return documents;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String[] getAdditionalExportHeaders() {
		String[] result = { DocumentCSVColumn.MUNICIPALITY_PORTAL_ID.toString() };
		return result;
	}

	@Override
	public Object[] getAdditionalExportValues(Document document) {
		Object[] result = { document.getLegalEntity().getMid() };
		return result;
	}
}
