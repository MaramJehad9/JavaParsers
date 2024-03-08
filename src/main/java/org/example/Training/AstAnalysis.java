package org.example.Training;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AstAnalysis {
    public static void main(String[] args) throws IOException {
        // Specify the directory containing the Java files
        Path path = Paths.get("C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\src\\main\\java\\org\\jhotdraw\\app\\");

        // Parse the Java source code files
        SourceRoot sourceRoot = new SourceRoot(path);
        sourceRoot.tryToParse("");

        // Visit and analyze the AST
        sourceRoot.getCompilationUnits().forEach(AstAnalysis::analyzeCompilationUnit);
    }

    private static void analyzeCompilationUnit(CompilationUnit cu) {
        // Visit class declarations
        cu.accept(new ClassVisitor(), null);
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
            super.visit(classDeclaration, arg);

            // Print class name
            System.out.println("Class: " + classDeclaration.getNameAsString());

            // Print extended types (inheritance)
            classDeclaration.getExtendedTypes().forEach(type -> {
                System.out.println("Extends: " + type.getNameAsString());
            });

            // Visit method invocations
            classDeclaration.accept(new MethodCallVisitor(), null);
        }
    }

    private static class MethodCallVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodCallExpr methodCallExpr, Void arg) {
            super.visit(methodCallExpr, arg);

            // Print method invocation details
            System.out.println("Method Call: " + methodCallExpr.getNameAsString());
            // You can access other details of method call expression here
        }
    }
}
