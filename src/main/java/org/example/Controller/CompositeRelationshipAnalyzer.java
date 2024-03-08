package org.example.Controller;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.ast.body.FieldDeclaration;


import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import org.example.Models.GraphBuilder;
import org.jgraph.graph.Edge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class CompositeRelationshipAnalyzer {
    static MutableGraph projectGraph;
    public static void main(String[] args) throws IOException {
        String directoryPath = "C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\";
        // Configure the symbol resolver
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolver(Paths.get(directoryPath)));

        projectGraph = mutGraph("project").setDirected(true);
        List<MutableGraph>subGraphs = new ArrayList<>();

        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setSymbolResolver(new JavaSymbolSolver(typeSolver));

        JavaParser javaParser = new JavaParser(parserConfiguration);
        projectGraph.add(mutNode("JHotDraw").add(Label.of("JHotDraw")));
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        ParseResult<CompilationUnit> parseResult = javaParser.parse(path);
                        if (parseResult.isSuccessful()) {
                            CompilationUnit cu = parseResult.getResult().orElseThrow(() -> new RuntimeException("Parsing failed"));
                            ClassVisitor classVisitor = new ClassVisitor();
                            cu.accept(classVisitor, null);
                            subGraphs.add(classVisitor.getGraph());
                            projectGraph.add(classVisitor.getGraph());

                        } else {
                            // Handle parsing failure
                            System.err.println("Parsing failed for file: " + path);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        String fileName = "wholeProjectGraph.png";
        try {
            Graphviz.fromGraph(projectGraph)
                    .render(Format.PNG)
                    .toFile(new File("C:\\Users\\MaramJehad\\Downloads\\PhD\\graphs\\" + fileName));
            System.out.println("Graph has been printed to: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to print the graph: " + e.getMessage());
        }

    }

    static class ClassVisitor extends VoidVisitorAdapter<Void> {

        static MutableGraph projectGraph;

        MutableGraph getGraph(){return projectGraph;}
        @Override
        public void visit(ClassOrInterfaceDeclaration cls, Void arg) {
            super.visit(cls, arg);
            projectGraph = mutGraph("project").setDirected(true);
            try {
                //analyzeClass(cls);
                MutableNode jHotDrawNode = projectGraph.nodes().stream()
                        .filter(node -> node.name().equals("jHotDraw"))
                        .findFirst()
                        .orElseGet(() -> {
                            MutableNode newNode = mutNode("jHotDraw").add(Shape.EGG);
                            projectGraph.add(newNode);
                            return newNode;
                        });

                analyzeRelations(cls, projectGraph);
            } catch (UnsolvedSymbolException e) {
                // Handle unsolved symbol exception
                System.err.println("UnsolvedSymbolException occurred: " + e.getMessage());
            }
            String fileName = cls.getNameAsString() + "projectGraph.png";
            try {
                Graphviz.fromGraph(projectGraph)
                        .render(Format.PNG)
                        .toFile(new File("C:\\Users\\MaramJehad\\Downloads\\PhD\\graphs\\" + fileName));
                System.out.println("Graph has been printed to: " + fileName);
            } catch (IOException e) {
                System.err.println("Failed to print the graph: " + e.getMessage());
            }
        }

        private MutableGraph analyzeRelations(ClassOrInterfaceDeclaration cls, MutableGraph projectGraph) {

            // Create the class node if it doesn't already exist
            // Check if the class node already exists in the graph
//            MutableNode classNode = projectGraph.nodes().stream()
//                    .filter(node -> node.name().equals(cls.getNameAsString()))
//                    .findFirst()
//                    .orElseGet(() -> {
//                        // If the class node doesn't exist, create a new one
//                        Shape shape = cls.isLocalClassDeclaration() ? Shape.COMPONENT : Shape.RECTANGLE;
//                        MutableNode newNode = mutNode(cls.getNameAsString()).add(shape);
//                        //projectGraph.add(newNode);
//                        return newNode;
//                    });

            projectGraph.add(mutNode("JHotDraw Project").add(Label.of("JHotDraw Project")).add(Shape.DIAMOND));
            // Analyze extended types
            projectGraph.add(analyzeExtendTypes(cls, mutNode(cls.getNameAsString())));

            // Analyze implemented types
            projectGraph.add(analyzeImplementedTypes(cls, mutNode(cls.getNameAsString())));

            return projectGraph;
        }

        private void analyzeImplementTypes(ClassOrInterfaceDeclaration cls, MutableNode classNode) {
            // Analyze implemented types
            // Analyze implemented types (interfaces)
            for (ClassOrInterfaceType implementedType : cls.getImplementedTypes()) {
                String relation = implementedType.toString();

                // Skip adding the class node if it already exists
                Optional<MutableNode> existingNode = projectGraph.nodes().stream()
                        .filter(node -> node.name().equals(relation))
                        .findFirst();
                //Shape shape = cls.isLocalClassDeclaration() ? Shape.COMPONENT : Shape.RECTANGLE;

                MutableNode parentNode;
                if (existingNode.isPresent()) {
                    parentNode = existingNode.get();
                } else {
                    // Node doesn't exist, create a new node and add it to the graph
                    parentNode = mutNode(relation).add(Shape.M_SQUARE).add(Label.of(implementedType.toString()+"\n<implements>"));
                    projectGraph.add(parentNode);
                }
                classNode.add(parentNode);
            }

            }

        private MutableGraph analyzeExtendTypes(ClassOrInterfaceDeclaration cls, MutableNode classNode) {
            MutableGraph subGraph = mutGraph(cls.getNameAsString()).setDirected(true);
            subGraph.add(mutNode(cls.getNameAsString()+"\n<extends>"));
            for (ClassOrInterfaceType extendedType : cls.getExtendedTypes()) {
                MutableNode currentNode;

                // Check if the parent node already exists in the graph
                Optional<MutableNode> existingParent = projectGraph.nodes().stream()
                        .filter(node -> node.name().equals(extendedType.getNameAsString()))
                        .findFirst();

                if (existingParent.isPresent()) {
                    currentNode = existingParent.get();
                } else {
                    // Parent node doesn't exist, create a new node and add it to the graph
                    currentNode = mutNode(extendedType.getNameAsString()).add(Shape.OVAL).add(Label.of(extendedType.getNameAsString()));
                    subGraph.add(currentNode);
                }

                // Add a link from the current class node to its base class node
                subGraph.addLink(currentNode);
            }
            return subGraph;
        }

        private MutableGraph analyzeImplementedTypes(ClassOrInterfaceDeclaration cls, MutableNode classNode) {
            MutableGraph subGraph = mutGraph(cls.getNameAsString()).setDirected(true);
            subGraph.add(mutNode(cls.getNameAsString()+"\n<implements>"));
            for (ClassOrInterfaceType implementedType : cls.getImplementedTypes()) {
                MutableNode currentNode;

                // Check if the parent node already exists in the graph
                Optional<MutableNode> existingParent = projectGraph.nodes().stream()
                        .filter(node -> node.name().equals(implementedType.getNameAsString()))
                        .findFirst();

                if (existingParent.isPresent()) {
                    currentNode = existingParent.get();
                } else {
                    // Parent node doesn't exist, create a new node and add it to the graph
                    currentNode = mutNode(implementedType.getNameAsString()).add(Shape.COMPONENT).add(Label.of(implementedType.getNameAsString()));
                    subGraph.add(currentNode);
                }

                // Add a link from the current class node to its base class node
                classNode.addLink(currentNode);
            }
            return subGraph;
        }

        private void analyzeClass(ClassOrInterfaceDeclaration cls) {
            List<MethodDeclaration> methodsDec = cls.findAll(MethodDeclaration.class);
            MutableGraph classGraph = mutGraph(cls.getNameAsString()).setDirected(true);

            MutableNode classNode = mutNode(cls.getNameAsString());
            classGraph.add(classNode);

            for (MethodDeclaration method : methodsDec){
                MutableNode methodNode = mutNode(method.getNameAsString());
                classNode.addLink(methodNode); // Add a link from classNode to methodNode
            }
            String fileName = cls.getNameAsString() + ".png";
            try {
                Graphviz.fromGraph(classGraph)
                        .render(Format.PNG)
                        .toFile(new File("C:\\Users\\MaramJehad\\Downloads\\PhD\\graphs\\" + fileName));
                System.out.println("Graph has been printed to: " + fileName);
            } catch (IOException e) {
                System.err.println("Failed to print the graph: " + e.getMessage());
            }
        }


        private void analyzeMethodInvocation(MethodCallExpr invocation) {
            String methodName = invocation.getNameAsString();
            String declaringClassName = invocation.getScope()
                    .map(expr -> expr.calculateResolvedType().describe()).orElse(null);
            if (declaringClassName != null && methodName != null) {
                if (methodName.equals(declaringClassName.substring(declaringClassName.lastIndexOf(".") + 1))) {
                    System.out.println("Object Instantiation: " + declaringClassName);
                }
            }
        }
    }
}
