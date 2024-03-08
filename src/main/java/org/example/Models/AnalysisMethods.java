package org.example.Models;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AnalysisMethods {
    private Map<String, MethodsAnalysis> ClassMethodsAnalysisMap;

    public static void main(String[] args) {
        try {
            // Open the existing Excel file
            //FileInputStream inputStream = new FileInputStream("existingWorkbook.xlsx");
            //Workbook workbook = new XSSFWorkbook(inputStream);
            Workbook workbook = new XSSFWorkbook();
            CreationHelper createHelper = workbook.getCreationHelper();

            // Close the input stream after reading
            //inputStream.close();

            SourceRoot sourceRoot = new SourceRoot(Paths.get("C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\src\\main\\java\\org\\jhotdraw\\app\\"));

            sourceRoot.tryToParse("");

            List<CompilationUnit> compilationUnits= sourceRoot.getCompilationUnits();

            // Iterate through each compilation unit (class)
            for (CompilationUnit unit: compilationUnits) {
                // Get or create a sheet for each class
                String className = unit.getPrimaryTypeName().orElse("Unknown");
                Sheet sheet = workbook.getSheet(className);
                if (sheet == null) {
                    sheet = workbook.createSheet(className);
                }

                // Create a visitor to calculate Cyclomatic Complexity for methods
                MethodCCVisitor methodCCVisitor = new MethodCCVisitor(sheet);

                // Visit each method in the class
                unit.accept(methodCCVisitor, null);
            }

            // Write the modified workbook content back to the file
            try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\src\\main\\java\\org\\jhotdraw\\app\\existingWorkbook.xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class MethodCCVisitor extends VoidVisitorAdapter<Void> {
        private final Sheet sheet;
        private int rowNum = 0;

        public MethodCCVisitor(Sheet sheet) {
            this.sheet = sheet;
        }



        @Override
        public void visit(MethodDeclaration n, Void arg) {
            // Calculate Cyclomatic Complexity for the current method
            int cc = calculateMethodsCyclomaticComplexity(n);
            String methodName = n.getNameAsString();

            // Create a new row in the sheet
            Row row = sheet.createRow(rowNum++);

            // Write method name and Cyclomatic Complexity value to the row
            row.createCell(0).setCellValue(methodName);
            row.createCell(1).setCellValue(cc);

            // Continue visiting other nodes
            super.visit(n, arg);
        }

        private int calculateMethodsCyclomaticComplexity(MethodDeclaration method) {
            int complexity = 1; // Start with 1 for the method itself
            BlockStmt body = method.getBody().orElse(null);

            if (body != null) {
                // Count the number of control flow statements
                for (Statement statement : body.getStatements()) {
                    if (statement.isIfStmt() || statement.isSwitchStmt() || statement.isWhileStmt() ||
                            statement.isDoStmt() || statement.isForStmt() || statement.isForEachStmt() ||
                            statement.isTryStmt()) {
                        complexity++;
                    }
                    // Additional complexity for each catch clause
                    else if (statement.isTryStmt()) {
                        TryStmt tryStmt = statement.asTryStmt();
                        complexity += tryStmt.getCatchClauses().size();
                    }
                }
            }

            return complexity;
        }

        public Sheet getSheet() {
            return sheet;
        }

        public int getRowNum() {
            return rowNum;
        }

    }
    }
