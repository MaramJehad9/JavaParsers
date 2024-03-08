package org.example.Controller;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Models.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OOMatricesExcelWriter {

    private static String dirPath;
    private static SourceRoot sourceRoot;
    private static Workbook workbook;
    private static CreationHelper createHelper;
    private static List<CompilationUnit> compilationUnits;


    public static void main(String[] args) throws IOException {
        dirPath = "C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\";

        workbook = new XSSFWorkbook();
        createHelper = workbook.getCreationHelper();

        sourceRoot = new SourceRoot(Paths.get(dirPath));
        sourceRoot.tryToParse("");

        AnalysisVisitor analysisVisitor = new AnalysisVisitor();
        compilationUnits = sourceRoot.getCompilationUnits();
        compilationUnits.forEach(cu -> cu.accept(analysisVisitor, null));

        exportToExcel( analysisVisitor.getClassInfoMap(), "C:\\Users\\MaramJehad\\Downloads\\PhD\\matrices\\" + "matrices01.xlsx");
        System.out.println("Excel file is exported successfully.");


        workbook.close();
    }


    private static void exportToExcel(Map<String, ClassInfo> classInfoMap, String filePath) throws IOException {
        // Create a new workbook

        Sheet classInfoSheet = workbook.createSheet("Class Information");
        // Create header row for class info sheet
        Row classInfoHeaderRow = classInfoSheet.createRow(0);
        classInfoHeaderRow.createCell(0).setCellValue("Class Name");
        classInfoHeaderRow.createCell(1).setCellValue("Number of Methods");
        classInfoHeaderRow.createCell(2).setCellValue("Number of Attributes");
        classInfoHeaderRow.createCell(3).setCellValue("Number of Lines of Code");
        classInfoHeaderRow.createCell(4).setCellValue("Extended Classes");
        classInfoHeaderRow.createCell(5).setCellValue("Composition");
        classInfoHeaderRow.createCell(6).setCellValue("Num Of Constructors");
        classInfoHeaderRow.createCell(7).setCellValue("Num Of Child Classes");
        classInfoHeaderRow.createCell(8).setCellValue("MethodsInvocations");
        classInfoHeaderRow.createCell(9).setCellValue("MethodsCC");
        classInfoHeaderRow.createCell(10).setCellValue("Fields");


        // Write class-related information to the sheet
        int classInfoRowNum = 1;
        int ii = 0;
        for (Map.Entry<String, ClassInfo> entry : classInfoMap.entrySet()) {
            Row row = classInfoSheet.createRow(classInfoRowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue().getNumOfMethods());
            row.createCell(2).setCellValue(entry.getValue().getNumOfAttributes());
            row.createCell(3).setCellValue(entry.getValue().getNumOfLines());
            row.createCell(6).setCellValue(entry.getValue().getNumOfConstructors());
            row.createCell(7).setCellValue(entry.getValue().getNumOfChildClasses());
            row.createCell(8).setCellValue(mapDatatoString(entry.getValue().getMethodInvocations()));
            row.createCell(9).setCellValue(listDataToString(entry.getValue().getMethodInfos()));;
            row.createCell(10).setCellValue(listDataToString2(entry.getValue().getFields()));

            StringBuilder extendedClassesStr = new StringBuilder();
            for (String extendedClass : entry.getValue().getExtendedClasses()) {
                extendedClassesStr.append(extendedClass).append(", ");
            }
            row.createCell(4).setCellValue(extendedClassesStr.toString());

            row.createCell(5).setCellValue(entry.getValue().hasComposition() ? "Yes" : "No");
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

    }

    private static String listDataToString(List<MethodInfo> methodInfos) {

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (MethodInfo entry : methodInfos) {
            sb.append(entry.getMethodName()).append("= ").append(entry.getMethodCyclomaticComplexity()).append(", ");
        }
        sb.append("}");
        return sb.toString();
    }


    private static String listDataToString2(List<String> methodInfos) {

        StringBuilder sb = new StringBuilder();
        sb.append("Fields = {");
        for (String entry : methodInfos) {
            sb.append(entry).append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
    private static String mapDatatoString(Map<String, Set<String>> mapData) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : mapData.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
        }

}
