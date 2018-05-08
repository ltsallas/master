package wifi4eu.wifi4eu.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import wifi4eu.wifi4eu.common.dto.model.BeneficiaryListItemDTO;
import wifi4eu.wifi4eu.entity.beneficiary.BeneficiaryListItem;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelExportGenerator<T> {
    private List<T> data;
    private Class dataClass;
    private ArrayList<String> fieldNames;
    private ArrayList<String> displayedFieldNames;

    public ExcelExportGenerator(List<T> data, Class dataClass) {
        this.data = data;
        this.dataClass = dataClass;
        generateFields();
    }

    public ByteArrayOutputStream exportExcelFile(String sheetName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < displayedFieldNames.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(displayedFieldNames.get(i));
                cell.setCellStyle(headerCellStyle);
            }
            int rowIndex = 1;
            for (T obj : data) {
                Row row = sheet.createRow(rowIndex);
                for (int i = 0; i < fieldNames.size(); i++) {
                    Cell cell = row.createCell(i);
                    Field field = obj.getClass().getDeclaredField(fieldNames.get(i));
                    field.setAccessible(true);
                    cell.setCellValue(generateCellValue(field, obj));
                }
                rowIndex++;
            }
            for (int i = 0; i < fieldNames.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(baos);
            baos.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos;
    }

    private void generateFields() {
        fieldNames = new ArrayList<>();
        displayedFieldNames = new ArrayList<>();
        if (dataClass == BeneficiaryListItem.class || dataClass == BeneficiaryListItemDTO.class) {
            fieldNames.add("lauId");
            displayedFieldNames.add("Lau ID");
            fieldNames.add("countryCode");
            displayedFieldNames.add("Country");
            fieldNames.add("name");
            displayedFieldNames.add("Municipality");
            fieldNames.add("counter");
            displayedFieldNames.add("Number of registrations");
            fieldNames.add("status");
            displayedFieldNames.add("Status");
            fieldNames.add("mediation");
            displayedFieldNames.add("Mediation");
//            fieldNames.add("issueStatus");
//            displayedFieldNames.add("Issue");
        } else {
            for (Field field : dataClass.getDeclaredFields()) {
                String fieldName = field.getName();
                fieldNames.add(fieldName);
                String displayedFieldName = String.valueOf(fieldName.charAt(0)).toUpperCase();
                if (fieldName.length() > 1) {
                    displayedFieldName += fieldName.substring(1);
                }
                displayedFieldNames.add(displayedFieldName);
            }
        }
    }

    private String generateCellValue(Field field, T objectData) throws IllegalAccessException {
        String value = "";
        if (field.get(objectData) != null) {
            if (dataClass == BeneficiaryListItem.class || dataClass == BeneficiaryListItemDTO.class) {
                switch (field.getName()) {
                    case "status":
                        if ((boolean) field.get(objectData)) {
                            value = "Applied";
                        } else {
                            value = "Registered";
                        }
                        break;
                    case "mediation":
                        if ((boolean) field.get(objectData)) {
                            value = "YES";
                        } else {
                            value = "NO";
                        }
                        break;
                    case "issueStatus":
                        int issueStatus = (int) field.get(objectData);
                        switch (issueStatus) {
                            case 1:
                            case 2:
                            case 3:
                                value = "WARNING " + issueStatus;
                                break;
                            default:
                                value = "";
                                break;
                        }
                        break;
                    default:
                        value = field.get(objectData).toString();
                        break;
                }
            } else {
                value = field.get(objectData).toString();
            }
        }
        return value;
    }
}