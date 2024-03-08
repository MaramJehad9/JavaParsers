package org.example.Controller;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CompositeRelationshipFinder {

    private Map<String, Set<String>> classDependencies;
    private Set<String> existingClasses; // Maintain a set of existing classes

    public CompositeRelationshipFinder() {
        this.classDependencies = new HashMap<>();
        this.existingClasses = new HashSet<>();
    }

    public Map<String, Set<String>> getClassDependencies() {
        return classDependencies;
    }
    public void analyzeProject(String projectPath) {

        File directory = new File(projectPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Invalid directory: " + projectPath);
            return;
        }

        traverseDirectory(directory);
    }

    private void traverseDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    traverseDirectory(file); // Recursive call for subdirectories
                } else if (file.getName().endsWith(".java")) {
                    analyzeJavaFile(file);
                }
            }
        }
    }
    private void analyzeJavaFile(File file) {
        String projectPath = "C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\";

        try {
            InputStream inputStream = new FileInputStream(file.getAbsolutePath());
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(inputStream);
            CompilationUnit cu = parseResult.getResult().orElseThrow(() -> new RuntimeException("Unable to parse the file"));

            cu.accept(new ClassVisitor(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
            private class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
            String className = classDeclaration.getNameAsString();
            Set<String> dependencies = new HashSet<>();

            // Check for composition relationships in fields
            for (FieldDeclaration fieldDeclaration : classDeclaration.getFields()) {
                String fieldType = fieldDeclaration.getElementType().toString();
                if (!fieldType.equals("int") && !fieldType.equals("double") && !fieldType.equals("float")) {
                    dependencies.add(fieldType);
                }
            }

            // Store dependencies for the class
            classDependencies.put(className, dependencies);

            // Add the class to the set of existing classes
            existingClasses.add(className);
        }
    }

    public void generateDOTFile(String outputPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            writer.println("digraph CompositeRelationships {");
            for (String className : classDependencies.keySet()) {
                if (existingClasses.contains(className)) {
                    Set<String> dependencies = classDependencies.get(className);
                    for (String dependency : dependencies) {
                        if (existingClasses.contains(dependency)) {
                            writer.println("\t\"" + className + "\" -> \"" + dependency + "\";");
                        }
                    }
                }
            }
            writer.println("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderGraph(String dotFilePath, String outputImageFile) throws IOException {
        Graphviz.fromFile(new File(dotFilePath)).render(Format.PNG).toFile(new File(outputImageFile));
    }



    public void printCompositeRelationships() {
        System.out.println("Composite Relationships:");

        for (String className : classDependencies.keySet()) {
            Set<String> dependencies = classDependencies.get(className);
            for (String dependency : dependencies) {
                // Check if the dependency exists in the set of existing classes
                if (existingClasses.contains(dependency)) {
                    System.out.println(className + " contains " + dependency);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\";
        CompositeRelationshipFinder finder = new CompositeRelationshipFinder();
        finder.analyzeProject(path); // Change this to your project path
        finder.printCompositeRelationships();
        finder.generateDOTFile("composite_relationships.dot");
        finder.renderGraph("composite_relationships.dot", path+"composite_relationships.png");

    }
}
