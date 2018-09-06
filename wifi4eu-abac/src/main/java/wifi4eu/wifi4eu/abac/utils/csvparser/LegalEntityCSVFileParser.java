package wifi4eu.wifi4eu.abac.utils.csvparser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import wifi4eu.wifi4eu.abac.data.dto.LegalEntityInformationCSVRow;
import wifi4eu.wifi4eu.abac.data.entity.LegalEntity;
import wifi4eu.wifi4eu.abac.data.enums.LegalEntityCSVColumn;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class LegalEntityCSVFileParser extends AbstractCSVFileParser {

	@Override
	protected Boolean validateColumns(CSVParser csvParser) {
		return super.validateColumns(csvParser,
				LegalEntityCSVColumn.MUNICIPALITY_PORTAL_ID,
				LegalEntityCSVColumn.MUNICIPALITY_NAME,
				LegalEntityCSVColumn.MUNICIPALITY_ADDRESS,
				LegalEntityCSVColumn.MUNICIPALITY_POSTAL_CODE,
				LegalEntityCSVColumn.MUNICIPALITY_PORTAL_ID,
				LegalEntityCSVColumn.MUNICIPALITY_CITY,
				LegalEntityCSVColumn.MUNICIPALITY_COUNTRY_CODE,
				LegalEntityCSVColumn.MUNICIPALITY_LANGUAGE_CODE,
				LegalEntityCSVColumn.MUNICIPALITY_REGISTRATION_NUMBER,
				LegalEntityCSVColumn.MUNICIPALITY_CALL_NUMBER,
				LegalEntityCSVColumn.MUNICIPALITY_ABAC_REFERENCE);
	}

	@Override
	protected List<LegalEntityInformationCSVRow> mapRowsToEntities(CSVParser csvParser) {

		List<LegalEntityInformationCSVRow> legalEntityRows = new ArrayList<>();

		for (CSVRecord csvRecord : csvParser) {

			LegalEntityInformationCSVRow legalEntityInformationCSVRow = new LegalEntityInformationCSVRow();

			legalEntityInformationCSVRow.setMid(Long.parseLong(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_PORTAL_ID)));
			legalEntityInformationCSVRow.setOfficialName(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_NAME));
			legalEntityInformationCSVRow.setAbacLatinName(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_ABAC_LATIN_NAME));
			legalEntityInformationCSVRow.setOfficialAddress(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_ADDRESS));
			legalEntityInformationCSVRow.setPostalCode(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_POSTAL_CODE));
			legalEntityInformationCSVRow.setCity(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_CITY));
			legalEntityInformationCSVRow.setCountryCode(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_COUNTRY_CODE));
			legalEntityInformationCSVRow.setLanguageCode(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_LANGUAGE_CODE));
			legalEntityInformationCSVRow.setRegistrationNumber(Long.parseLong(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_REGISTRATION_NUMBER)));
			legalEntityInformationCSVRow.setAbacReference(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_ABAC_REFERENCE));
			legalEntityInformationCSVRow.setCallNumber(Integer.parseInt(csvRecord.get(LegalEntityCSVColumn.MUNICIPALITY_CALL_NUMBER)));

			legalEntityRows.add(legalEntityInformationCSVRow);
		}
		return legalEntityRows;
	}

	public String exportLegalEntitiesToCSV(List<LegalEntity> legalEntities) {

		try {

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
			BufferedWriter writer = new BufferedWriter(streamWriter);

			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader(
							LegalEntityCSVColumn.MUNICIPALITY_PORTAL_ID.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_NAME.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_ABAC_LATIN_NAME.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_ADDRESS.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_POSTAL_CODE.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_CITY.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_COUNTRY_CODE.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_LANGUAGE_CODE.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_REGISTRATION_NUMBER.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_ABAC_REFERENCE.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_CALL_NUMBER.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_ABAC_STATUS.toString(),
							LegalEntityCSVColumn.MUNICIPALITY_ABAC_MESSAGE.toString()
					));

			for (LegalEntity legalEntity : legalEntities) {
				csvPrinter.printRecord(
						legalEntity.getMid(),
						legalEntity.getOfficialName(),
						legalEntity.getAbacLatinName(),
						legalEntity.getOfficialAddress(),
						legalEntity.getPostalCode(),
						legalEntity.getCity(),
						legalEntity.getCountry().getIso3Code(),
						legalEntity.getLanguageCode(),
						legalEntity.getRegistrationNumber(),
						legalEntity.getAbacFelId(),
						legalEntity.getCallNumber(),
						legalEntity.getWfStatus(),
						legalEntity.getRejectionReason() != null ? legalEntity.getRejectionReason() : legalEntity.getAbacErrorMessage()
				);
			}

			csvPrinter.flush();
			return stream.toString();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}