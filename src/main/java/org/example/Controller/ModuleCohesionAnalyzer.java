package org.example.Controller;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.example.Models.Module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ModuleCohesionAnalyzer {

    public static void main(String[] args) throws IOException {
        // Define the directory path of your Java project
        String projectDirectory = "path/to/your/java/project";

        // Traverse the directory and analyze each Java file
        List<Module> modules = analyzeJavaProject(projectDirectory);

        // Calculate cohesion for each module
        calculateCohesion(modules);

        // Print or store cohesion results for further analysis
        for (Module module : modules) {
            System.out.println("Module: " + module.getName());
            System.out.println("Functional Cohesion: " + module.getFunctionalCohesion());
            System.out.println("Data Flow Cohesion: " + module.getDataFlowCohesion());
            // Add more cohesion metrics if needed
            System.out.println();
        }
    }

    // Method to analyze the Java project and extract module information
    private static List<Module> analyzeJavaProject(String projectDirectory) throws IOException {
        List<Module> modules = new ArrayList<>();
        Files.walk(new File(projectDirectory).toPath())
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        JavaParser javaParser = new JavaParser();
                        ParseResult<CompilationUnit> parseResult = javaParser.parse(path.toFile());
                        if (parseResult.isSuccessful()) {
                            CompilationUnit cu = parseResult.getResult().orElse(null);
                            if (cu != null) {
                                Module module = ModuleCohesionAnalyzer.extractModuleInfo(cu);
                                modules.add(module);
                            } else {
                                System.out.println("Failed to parse file: " + path);
                            }
                        } else {
                            System.out.println("Parsing failed for file: " + path);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return modules;
    }

    // Method to extract module information from a CompilationUnit
    private static Module extractModuleInfo(CompilationUnit cu) {
        // Extract information like number of input parameters, local variables, etc.
        // Create and return a Module object with the extracted information
        Module module = new Module();
        module.setName(cu.getStorage().map(storage -> storage.getFileName().toString()).orElse(""));
        cu.accept(new ModuleInfoVisitor(module), null);

        return module;
    }

    // Method to calculate cohesion for each module
    private static void calculateCohesion(List<Module> modules) {
        for (Module module : modules) {
            // Calculate functional cohesion using provided formula
            module.setFunctionalCohesion(calculateFunctionalCohesion(module));
            // Calculate data flow cohesion using provided formula
            module.setDataFlowCohesion(calculateDataFlowCohesion(module));
            // Add more cohesion metrics calculation if needed
        }
    }

    // Method to calculate functional cohesion for a module
    private static double calculateFunctionalCohesion(Module module) {
        // Implement the functional cohesion calculation using the provided formula
        return 0.0; // Placeholder, replace with actual calculation
    }

    // Method to calculate data flow cohesion for a module
    private static double calculateDataFlowCohesion(Module module) {
        // Implement the data flow cohesion calculation using the provided formula
        return 0.0; // Placeholder, replace with actual calculation
    }
    private static class ModuleInfoVisitor extends VoidVisitorAdapter<Void> {
        private Module module;

        public ModuleInfoVisitor(Module module) {
            this.module = module;
        }

        @Override
        public void visit(Parameter parameter, Void arg) {
            // Count input and output parameters
            if (parameter.getParentNode().isPresent() && parameter.getParentNode().get() instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) parameter.getParentNode().get();
                if (method.getParameters().indexOf(parameter) == 0) {
                    // Input parameter (receiver parameter)
                    module.incrementInDataParams();
                } else {
                    // Output parameter
                    module.incrementOutDataParams();
                }
            }
            super.visit(parameter, arg);
        }

        @Override
        public void visit(VariableDeclarator variableDeclarator, Void arg) {
            // Count local variables
            module.incrementLocalDataVars();
            super.visit(variableDeclarator, arg);
        }

        @Override
        public void visit(FieldDeclaration fieldDeclaration, Void arg) {
            // Count global variables
            module.incrementGlobalDataVars();
            super.visit(fieldDeclaration, arg);
        }

        // You can implement visit methods for other node types to extract more information

    }
}
