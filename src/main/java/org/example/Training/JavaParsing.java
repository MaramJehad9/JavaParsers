package org.example.Training;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.io.File;
import java.io.FileNotFoundException;

public class JavaParsing {
    public static void main(String[] args) throws FileNotFoundException {
        // Parse the Java source code file
        JavaParser parser = new JavaParser();
        ParseResult<CompilationUnit> result = parser.parse(new File("C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\src\\main\\java\\org\\jhotdraw\\app\\AbstractView.java"));

        if (result.isSuccessful()) {
            CompilationUnit cu = result.getResult().orElseThrow(() -> new FileNotFoundException("Failed to parse the Java source file."));

            // Visit and traverse the AST to extract structural information
            cu.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
                    super.visit(classDeclaration, arg);
                    System.out.println("Class: " + classDeclaration.getNameAsString());
                }

                @Override
                public void visit(MethodDeclaration methodDeclaration, Void arg) {
                    super.visit(methodDeclaration, arg);
                    System.out.println("Method: " + methodDeclaration.getNameAsString());
                }
            }, null);
        } else {
            System.out.println("Failed to parse the Java source file.");
        }
    }
}
