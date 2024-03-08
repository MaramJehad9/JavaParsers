package org.example.Training;
import com.github.javaparser.JavaParser;
        import com.github.javaparser.ast.CompilationUnit;
        import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
        import com.github.javaparser.ast.expr.MethodCallExpr;
        import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
        import com.github.javaparser.utils.SourceRoot;

        import java.io.IOException;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Set;
        import java.util.HashSet;

public class AstAnalysisHashset {
    public static void main(String[] args) throws IOException {
        // Specify the directory containing the Java files
        Path path = Paths.get("C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\src\\main\\java\\org\\jhotdraw\\app\\");


        // Parse the Java source code files
        SourceRoot sourceRoot = new SourceRoot(path);
        sourceRoot.tryToParse("");

        // Create data structures to store method invocations and inheritance relationships
        Map<String, Set<String>> methodInvocations = new HashMap<>();
        Map<String, Set<String>> inheritanceRelationships = new HashMap<>();

        // Visit and analyze the AST
        sourceRoot.getCompilationUnits().forEach(cu -> cu.accept(new AnalysisVisitor(methodInvocations, inheritanceRelationships), null));

        // Print method invocations
        System.out.println("Method Invocations:");
        for (Map.Entry<String, Set<String>> entry : methodInvocations.entrySet()) {
            System.out.println("Class: " + entry.getKey());
            System.out.println("Methods Invoked: " + entry.getValue());
            System.out.println();
        }

        // Print inheritance relationships
        System.out.println("Inheritance Relationships:");
        for (Map.Entry<String, Set<String>> entry : inheritanceRelationships.entrySet()) {
            System.out.println("Class: " + entry.getKey());
            System.out.println("Extends: " + entry.getValue());
            System.out.println();
        }
    }

    private static class AnalysisVisitor extends VoidVisitorAdapter<Void> {
        private final Map<String, Set<String>> methodInvocations;
        private final Map<String, Set<String>> inheritanceRelationships;

        public AnalysisVisitor(Map<String, Set<String>> methodInvocations, Map<String, Set<String>> inheritanceRelationships) {
            this.methodInvocations = methodInvocations;
            this.inheritanceRelationships = inheritanceRelationships;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
            super.visit(classDeclaration, arg);

            // Get class name
            String className = classDeclaration.getNameAsString();

            // Get extended classes/interfaces
            Set<String> extendedClasses = new HashSet<>();
            classDeclaration.getExtendedTypes().forEach(type -> extendedClasses.add(type.getNameAsString()));
            inheritanceRelationships.put(className, extendedClasses);

            // Visit method invocations
            classDeclaration.accept(new MethodCallVisitor(className, methodInvocations), null); // Pass methodInvocations map
        }
    }


    private static class MethodCallVisitor extends VoidVisitorAdapter<Void> {
        private final String className;
        private final Map<String, Set<String>> methodInvocations;

        public MethodCallVisitor(String className, Map<String, Set<String>> methodInvocations) {
            this.className = className;
            this.methodInvocations = methodInvocations;
        }

        @Override
        public void visit(MethodCallExpr methodCallExpr, Void arg) {
            super.visit(methodCallExpr, arg);

            // Get method name
            String methodName = methodCallExpr.getNameAsString();

            // Add method invocation to the map
            methodInvocations.computeIfAbsent(className, k -> new HashSet<>()).add(methodName);
        }
    }

}
