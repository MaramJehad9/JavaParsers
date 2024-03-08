/*
package org.example.Models;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CreateExcelSheet {
    private Workbook workbook;
    private CreationHelper createHelper;
    private Sheet mySheet;
    private String sheetName;
    public CreateExcelSheet(String sheetName, Map<String, Set<String>> mysheet, String filePath){
        // Create a new workbook
        this.workbook = new XSSFWorkbook();
        this.createHelper = workbook.getCreationHelper();
        this.sheetName = sheetName;

        // Create a new sheet for method invocations
        this.mySheet = workbook.createSheet(sheetName);

        // Create header row for method invocations sheet
        Row mySheetHeaderRow = mySheet.createRow(0);
        mySheetHeaderRow.createCell(0).setCellValue("Class Name");
        mySheetHeaderRow.createCell(1).setCellValue(sheetName);

        // Write method invocations data to the sheet
        int methodInvocationsRowNum = 1;

    }
    private static void exportToExcel(Map<String, Set<String>> methodInvocations, Map<String, Set<String>> inheritanceRelationships, Map<String, ClassInfo> classInfoMap, String filePath) throws IOException {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a new sheet for method invocations
        Sheet methodInvocationsSheet = workbook.createSheet("Method Invocations");

        // Create header row for method invocations sheet
        Row methodInvocationsHeaderRow = methodInvocationsSheet.createRow(0);
        methodInvocationsHeaderRow.createCell(0).setCellValue("Class Name");
        methodInvocationsHeaderRow.createCell(1).setCellValue("Method Invocations");

        // Write method invocations data to the sheet
        int methodInvocationsRowNum = 1;
        for (Map.Entry<String, Set<String>> entry : methodInvocations.entrySet()) {
            Row row = methodInvocationsSheet.createRow(methodInvocationsRowNum++);
            row.createCell(0).setCellValue(entry.getKey());

            StringBuilder methodInvocationsStr = new StringBuilder();
            for (String methodName : entry.getValue()) {
                methodInvocationsStr.append(methodName).append(", ");
            }
            row.createCell(1).setCellValue(methodInvocationsStr.toString());
        }

        // Create a new sheet for inheritance relationships
        Sheet inheritanceRelationshipsSheet = workbook.createSheet("Inheritance Relationships");

        // Create header row for inheritance relationships sheet
        Row inheritanceRelationshipsHeaderRow = inheritanceRelationshipsSheet.createRow(0);
        inheritanceRelationshipsHeaderRow.createCell(0).setCellValue("Class Name");
        inheritanceRelationshipsHeaderRow.createCell(1).setCellValue("Extended Classes");

        // Write inheritance relationships data to the sheet
        int inheritanceRelationshipsRowNum = 1;
        for (Map.Entry<String, Set<String>> entry : inheritanceRelationships.entrySet()) {
            Row row = inheritanceRelationshipsSheet.createRow(inheritanceRelationshipsRowNum++);
            row.createCell(0).setCellValue(entry.getKey());

            StringBuilder extendedClassesStr = new StringBuilder();
            for (String extendedClass : entry.getValue()) {
                extendedClassesStr.append(extendedClass).append(", ");
            }
            row.createCell(1).setCellValue(extendedClassesStr.toString());
        }

        // Create a new sheet for class-related information
        Sheet classInfoSheet = workbook.createSheet("Class Information");

        // Create header row for class info sheet
        Row classInfoHeaderRow = classInfoSheet.createRow(0);
        classInfoHeaderRow.createCell(0).setCellValue("Class Name");
        classInfoHeaderRow.createCell(1).setCellValue("Number of Methods");
        classInfoHeaderRow.createCell(2).setCellValue("Number of Attributes");
        classInfoHeaderRow.createCell(3).setCellValue("Number of Lines of Code");
        classInfoHeaderRow.createCell(4).setCellValue("Extended Classes");
        classInfoHeaderRow.createCell(5).setCellValue("Composition");
        classInfoHeaderRow.createCell(6).setCellValue("Has Composition");
        classInfoHeaderRow.createCell(7).setCellValue("Has Inheritance");

        // Write class-related information to the sheet
        int classInfoRowNum = 1;
        for (Map.Entry<String, ClassInfo> entry : classInfoMap.entrySet()) {
            Row row = classInfoSheet.createRow(classInfoRowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue().getNumOfMethods());
            row.createCell(2).setCellValue(entry.getValue().getNumOfAttributes());
            row.createCell(3).setCellValue(entry.getValue().getNumOfLines());
            row.createCell(6).setCellValue(entry.getValue().getNumOfConstructors());
            row.createCell(7).setCellValue(entry.getValue().getNumOfChildClasses());

            StringBuilder extendedClassesStr = new StringBuilder();
            for (String extendedClass : entry.getValue().getExtendedClasses()) {
                extendedClassesStr.append(extendedClass).append(", ");
            }
            row.createCell(4).setCellValue(extendedClassesStr.toString());

            row.createCell(5).setCellValue(entry.getValue().hasComposition() ? "Yes" : "No");
        }

        // Resize columns to fit the content for all sheets
        for (int i = 0; i < 6; i++) {
            methodInvocationsSheet.autoSizeColumn(i);
            inheritanceRelationshipsSheet.autoSizeColumn(i);
            classInfoSheet.autoSizeColumn(i);
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        // Close the workbook
        workbook.close();
    }
}
*/
