package wifi4eu.wifi4eu.abac.utils.csvparser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Component;
import wifi4eu.wifi4eu.abac.data.dto.LegalCommitmentCSVRow;
import wifi4eu.wifi4eu.abac.data.entity.LegalCommitment;
import wifi4eu.wifi4eu.abac.data.enums.LegalCommitmentCSVColumn;
import wifi4eu.wifi4eu.abac.utils.DateTimeUtils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Component
public class LegalCommitmentCSVFileParser extends AbstractCSVFileParser{

	@Override
	protected Boolean validateColumns(CSVParser csvParser) throws RuntimeException {
		throw new NotImplementedException();
	}

	@Override
	protected List<LegalCommitmentCSVRow> mapRowsToEntities(CSVParser csvParser) {
		throw new NotImplementedException();
	}

	public String exportLegalCommitmentToCSV(List<LegalCommitment> legalCommitments) {

		try {

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
			BufferedWriter writer = new BufferedWriter(streamWriter);

			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader(
							LegalCommitmentCSVColumn.MUNICIPALITY_PORTAL_ID.toString(),
							LegalCommitmentCSVColumn.GRANT_AGREEMENT_SIGNATURE_DATE.toString(),
							LegalCommitmentCSVColumn.GRANT_AGREEMENT_COUNTERSIGNATURE_DATE.toString(),
							LegalCommitmentCSVColumn.ABAC_STATUS.toString(),
							LegalCommitmentCSVColumn.ABAC_MESSAGE.toString(),
							LegalCommitmentCSVColumn.ABAC_KEY.toString()
					));

			for (LegalCommitment legalCommitment : legalCommitments) {
				csvPrinter.printRecord(
						legalCommitment.getLegalEntity().getMid(),
						DateTimeUtils.format(legalCommitment.getGrantAgreementSignatureDate(), PORTAL_CSV_DATE_FORMAT),
						DateTimeUtils.format(legalCommitment.getGrantAgreementCounterSignatureDate(), PORTAL_CSV_DATE_FORMAT),
						legalCommitment.getWfStatus(),
						legalCommitment.getAbacErrorMessage(),
						legalCommitment.getAbacKey()
				);
			}

			csvPrinter.flush();
			csvPrinter.close();
			return stream.toString();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}