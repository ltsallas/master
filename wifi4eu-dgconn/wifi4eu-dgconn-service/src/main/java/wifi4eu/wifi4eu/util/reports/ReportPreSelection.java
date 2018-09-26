package wifi4eu.wifi4eu.util.reports;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import wifi4eu.wifi4eu.common.helper.Validator;
import wifi4eu.wifi4eu.common.utils.Utils;
import wifi4eu.wifi4eu.entity.location.NutCallCustom;
import wifi4eu.wifi4eu.repository.call.CallRepository;
import wifi4eu.wifi4eu.repository.location.NutCallCustomRepository;
import wifi4eu.wifi4eu.util.reporting.ReportingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportPreSelection {

    @Autowired
    CallRepository callRepository;

    @Autowired
    NutCallCustomRepository nutCallCustomRepository;


    static String[] fields = {"Number of applicants", "Number of pre-selected applicants", "Number of applicants in reserve list", "Number of unsuccessful applicants (not in the pre-selection list, not in the reserve list)", "Number of duplicates", "Number of duplicates invalidated", "Number of follow-up (supporting documents)", "Number of invalidated for reason 1: After the follow-up request, the applicant provided a document which was corrupt/impossible to open in the format supplied.", "Number of invalidated for reason 2: After the follow-up request, the applicant provided the same document(s) as originally supplied with the application.", "Number of invalidated for reason 3: After the follow-up request, the applicant provided a document which was unreadable.", "Number of invalidated for reason 4: After the follow-up request, the applicant provided a document which was incomplete", "Number of invalidated for reason 5: After the follow-up request, the applicant provided a document which was incorrect/did not correspond to the required document (or still contained incorrect information).", "Number of invalidated for reason 6: After the follow-up request, the applicant provided a document which was missing a signature.", "Number of invalidated for reason 7: The deadline for the request of correction of the required supporting documents passed without compliance by the applicant.", "Number of invalidated for reason 8: The application included a merged municipality.", "Number of invalidated for reason 9: Due to irregularities found in the application, it was invalidated."};
    static String[] totalValues = {"applicants", "preSelectedApplicants", "reservedApplicants", "unsuccessfulApplicants", "duplicates", "duplicatesInvalidated", "followUp", "invalidatedReason1", "invalidatedReason2", "invalidatedReason3", "invalidatedReason4", "invalidatedReason5", "invalidatedReason6", "invalidatedReason7", "invalidatedReason8", "invalidatedReason9"};

    public void generate(HSSFWorkbook workbook) {
        if (Validator.isNotNull(callRepository.getIdCurrentCall())) {
            HSSFSheet sheet = workbook.createSheet("State of play Report");
            int numColumn = 0;
            HSSFRow firstRow = sheet.createRow((short) numColumn);
            firstRow.createCell(0).setCellValue(callRepository.getNameCurrentCall());
            firstRow.createCell(1).setCellValue("Total");
            numColumn++;
            HSSFRow row;
            for (int i = numColumn; i <= fields.length; i++) {
                row = sheet.createRow((short) i);
                row.createCell(0).setCellValue(fields[i - 1]);
            }
            generateTotalValues(sheet, 0);
            putCountriesCallOpen(sheet, firstRow, 2);
            ReportingUtils.autoSizeColumns(workbook);
        } else {
            // send email notifying that no open call is available?
            System.out.println("No call open found");
        }
    }

    private void generateTotalValues(HSSFSheet sheet, int idNut) {
        int column = 1;
        Map<String, Object> allValues = getAllValues(idNut);
        HSSFRow row;
        int takeNumber = 0;
        for (int i = 1; i <= totalValues.length; i++) {
            row = sheet.getRow(i);
            String valueSet = allValues.get(totalValues[takeNumber]).toString();
            if (Validator.isNull(valueSet)) valueSet = "0";
            row.createCell(column).setCellValue(valueSet);
            takeNumber++;
        }
    }

    private void putCountriesCallOpen(HSSFSheet sheet, HSSFRow row, int numColumn) {
        ArrayList<NutCallCustom> nuts = nutCallCustomRepository.findNutsAndCallNameByCurrentCall();
        HSSFRow intialRow = row;
        int[] passedNuts = new int[nuts.size()];
        int a = 0;
        for (NutCallCustom nut : nuts) {
            if (!Utils.contains(passedNuts, nut.getId())) {
                row.createCell(numColumn).setCellValue(nut.getLabel());
                Map<String, Object> allValues = getAllValues(nut.id);
                String[] totalValues = {"applicants", "preSelectedApplicants", "reservedApplicants", "unsuccessfulApplicants", "duplicates", "duplicatesInvalidated", "followUp", "invalidatedReason1", "invalidatedReason2", "invalidatedReason3", "invalidatedReason4", "invalidatedReason5", "invalidatedReason6", "invalidatedReason7", "invalidatedReason8", "invalidatedReason9"};
                int takeNumber = 0;
                for (int i = 1; i <= totalValues.length; i++) {
                    row = sheet.getRow(i);
                    String valueSet = "" + allValues.get(totalValues[takeNumber]);
                    row.createCell(numColumn).setCellValue(valueSet);
                    takeNumber++;
                }
                numColumn++;
                row = intialRow;
                passedNuts[a] = nut.getId();
                a++;
            }
        }
    }

    @Transactional
    private Map<String, Object> getAllValues(int idNut) {
        Map<String, Object> mapResult = new HashMap<>();
        int applicants = getAllApplicants(idNut);
        int preselectedApplicants = getPreselectedApplicants(idNut);
        int reservedApplicants = getReservedApplicants(idNut);
        int unsuccessfulApplicants = getUnsuccessfulApplicant(idNut);
        int duplicates = getDuplicates(idNut);
        int duplicatesInvalidated = getApplicantsWithWarnings(2, idNut);
        int followUp = getApplicantsWithWarnings(3, idNut);
        int invalidatedReason1 = getNumberDuplicates(idNut);
        int invalidatedReason2 = getNumberDuplicatesInvalidated(idNut);
        int invalidatedReason3 = getNumberInvalidatedByReason(1, idNut);
        int invalidatedReason4 = getNumberInvalidatedByReason(2, idNut);
        int invalidatedReason5 = getNumberInvalidatedByReason(5, idNut);
        int invalidatedReason6 = getNumberInvalidatedByReason(6, idNut);
        int invalidatedReason7 = getNumberInvalidatedByReason(7, idNut);
        int invalidatedReason8 = getNumberInvalidatedByReason(8, idNut);
        int invalidatedReason9 = getNumberInvalidatedByReason(9, idNut);
        mapResult.put("applicants", applicants);
        mapResult.put("preSelectedApplicants", preselectedApplicants);
        mapResult.put("reservedApplicants", reservedApplicants);
        mapResult.put("unsuccessfulApplicants", unsuccessfulApplicants);
        mapResult.put("duplicates", duplicates);
        mapResult.put("duplicatesInvalidated", duplicatesInvalidated);
        mapResult.put("followUp", followUp);
        mapResult.put("invalidatedReason1", invalidatedReason1);
        mapResult.put("invalidatedReason2", invalidatedReason2);
        mapResult.put("invalidatedReason3", invalidatedReason3);
        mapResult.put("invalidatedReason4", invalidatedReason4);
        mapResult.put("invalidatedReason5", invalidatedReason5);
        mapResult.put("invalidatedReason6", invalidatedReason6);
        mapResult.put("invalidatedReason7", invalidatedReason7);
        mapResult.put("invalidatedReason8", invalidatedReason8);
        mapResult.put("invalidatedReason9", invalidatedReason9);
        return mapResult;
    }

    @Transactional
    private int getAllApplicants(int idNut) {
        if (idNut != 0) {
            return (int) applicationRepository.countApplicationsForCurrentCall(idNut);
        } else {
            return (int) applicationRepository.countApplicationsForCurrentCall();
        }
    }

}
