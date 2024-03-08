package org.example.Controller;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;

public class ClassGraphBuilder {
    public static void main(String[] args) throws IOException {
       analyzeClassRelations();
    }
    private static void analyzeClassRelations() throws IOException {
        String directoryPath = "C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\";
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolver(Paths.get(directoryPath)));
        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setSymbolResolver(new JavaSymbolSolver(typeSolver));

        JavaParser javaParser = new JavaParser(parserConfiguration);
        Map<String,List<String>>classMap = new HashMap<>();
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
                            classMap.putAll(classVisitor.getClasses());
                        } else {
                            // Handle parsing failure
                            System.err.println("Parsing failed for file: " + path);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

       extractsInterfaceNodes(classMap, mutGraph("jHotDraw").setDirected(true));
       extractsClassNode(classMap, mutGraph("jHotDraw").setDirected(true));
    }
    public static void outGraph(Map<String,List<String>> classMap){
        MutableGraph projectGraph = mutGraph("Project").setDirected(true);
        projectGraph.add(mutNode("JHotDraw").add(Label.of("JHotDraw")));

        for (Map.Entry<String, List<String>> entry : classMap.entrySet()) {
            String className = entry.getKey();
            List<String> relations = entry.getValue();

            MutableNode classNode = mutNode(className.substring(0,className.indexOf("*"))).add(Shape.OVAL);
            projectGraph.add(classNode);
            System.out.println(className.substring(className.indexOf("*")));

            // Add extends relation
            if (className.substring(className.indexOf("*")).equals("*implemented")) {
                System.out.println("Adding " + className + " extends" + relations.toString());
                for (String relation : relations) {
                    MutableNode node = mutNode(relation).add(Label.of(relation));

                    // Check if the node already exists in the graph
                    Optional<MutableNode> existingNode = projectGraph.nodes().stream()
                            .filter(n -> n.name().equals(node.name()))
                            .findFirst();

                    if (existingNode.isPresent()) {
                        // If the node exists, modify the edge label
                        MutableNode existing = existingNode.get();
                        existing.addLink(classNode).add(Label.of("implements")); // Modify the label as needed
                    } else {
                        // If the node doesn't exist, add it to the graph
                        projectGraph.add(node);
                        projectGraph.addLink(classNode, node);
                    }
                }
            }else if (className.substring(className.indexOf("*")).equals("*extends")) {
                for (String relation : relations) {
                    MutableNode node = mutNode(relation).add(Label.of(relation));

                    // Check if the node already exists in the graph
                    Optional<MutableNode> existingNode = projectGraph.nodes().stream()
                            .filter(n -> n.name().equals(node.name()))
                            .findFirst();

                    if (existingNode.isPresent()) {
                        // If the node exists, modify the edge label
                        MutableNode existing = existingNode.get();
                        existing.addLink(classNode).add(Label.of("extends")); // Modify the label as needed
                    } else {
                        // If the node doesn't exist, add it to the graph
                        projectGraph.add(node);
                    }
                }
            }
        }
        extractsInterfaceNodes(classMap, projectGraph);

    }

    private static void outToPNG(MutableGraph projectGraph) {
        String name = projectGraph.toString().substring(10,18);
        String fileName = "C:\\Users\\MaramJehad\\Downloads\\PhD\\graphs\\projectGraph"+name+".png"; // Define the file name for the PNG picture
        System.out.println(projectGraph.toString());
        try {
            Graphviz.fromGraph(projectGraph)
                    .render(Format.PNG)
                    .toFile(new File(fileName)); // Save the PNG picture to a file
            System.out.println("Graph has been printed to: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to print the graph: " + e.getMessage());
        }
    }

    private MutableNode getNodeByName(MutableGraph graph, String nodeName) {
        for (MutableNode node : graph.nodes()) {
            if (node.name().equals(nodeName)) {
                return node;
            }
        }
        return null; // Node not found
    }

    private static void extractsInterfaceNodes(Map<String, List<String>> classes, MutableGraph graph) {
        MutableGraph interfaceGraph =  mutGraph("interfaces").setDirected(true);
        MutableGraph abstractClassGraph = mutGraph("abstracts").setDirected(true);
        for (Map.Entry<String, List<String>> entry : classes.entrySet()) {
            String className = entry.getKey();
            List<String> relations = entry.getValue();

            String nodeName = className.substring(0, className.indexOf("*"));

            Optional<MutableNode> existingNode = interfaceGraph.nodes().stream()
                    .filter(n -> n.name().equals(nodeName))
                    .findFirst();

            if (!existingNode.isPresent()) {

                if (className.substring(className.indexOf("*")).equals("*implemented")) {
                    if (relations.size() > 0) {
                        for (String interfaceName : relations) {
                            Optional<MutableNode> existingInterface = interfaceGraph.nodes().stream()
                            .filter(n -> n.name().equals(interfaceName))
                            .findFirst();
                            MutableNode classNode = mutNode(nodeName).add(Label.of(nodeName+"\n<<implements>>")).add(Shape.OVAL);
                            interfaceGraph.add(classNode);

                            if (!existingInterface.isPresent())
                            {
                                MutableNode interfaceNode = mutNode(interfaceName).add(Shape.RECTANGLE).add(Label.of(interfaceName + "\n\"interface\""));
                                interfaceGraph.add(interfaceNode);
                                classNode.addLink(interfaceNode);
                            }else{
                                classNode.addLink(existingInterface.get());
                            }
                        }

                    }
                }
            }else{
                if (className.substring(className.indexOf("*")).equals("*implemented")) {
                    System.out.println("existingNodesClasses"+existingNode.get().toString());
                    if (relations.size() > 0) {
                        for (String interfaceName : relations) {
                            Optional<MutableNode> existingInterface = interfaceGraph.nodes().stream()
                                    .filter(n -> n.name().equals(interfaceName))
                                    .findFirst();

                            if (!existingInterface.isPresent())
                            {
                                MutableNode interfaceNode = mutNode(interfaceName).add(Shape.RECTANGLE).add(Label.of(interfaceName + "\ninterface"));
                                interfaceGraph.add(interfaceNode);
                                interfaceGraph.addLink(existingNode.get(), interfaceNode);

                            }else{
                                interfaceGraph.addLink(existingNode.get(), existingInterface.get());
                            }
                        }

                    }
                }
            }
        }
        graph.add(interfaceGraph);

        outToPNG(interfaceGraph);
    }

    private static void extractsClassNode(Map<String, List<String>> classes, MutableGraph graph) {
        MutableGraph abstractClassGraph = mutGraph("abstracts").setDirected(true);
        for (Map.Entry<String, List<String>> entry : classes.entrySet()) {
            String className = entry.getKey();
            List<String> relations = entry.getValue();

            String nodeName = className.substring(0, className.indexOf("*"));

            Optional<MutableNode> existingNode = abstractClassGraph.nodes().stream()
                    .filter(n -> n.name().equals(nodeName))
                    .findFirst();

            if (!existingNode.isPresent()) {

                if (className.substring(className.indexOf("*")).equals("*extends")) {
                    if (relations.size() > 0) {
                        for (String interfaceName : relations) {
                            Optional<MutableNode> existingInterface = abstractClassGraph.nodes().stream()
                                    .filter(n -> n.name().equals(interfaceName))
                                    .findFirst();
                            MutableNode classNode = mutNode(nodeName).add(Label.of(nodeName+"\n<<extends>>")).add(Shape.OVAL);
                            abstractClassGraph.add(classNode);

                            if (!existingInterface.isPresent())
                            {
                                MutableNode interfaceNode = mutNode(interfaceName).add(Shape.OVAL).add(Label.of(interfaceName + "\n\"baseClass\""));
                                abstractClassGraph.add(interfaceNode);
                                classNode.addLink(interfaceNode);
                            }else{
                                classNode.addLink(existingInterface.get());
                            }
                        }

                    }
                }
            }else{
                if (className.substring(className.indexOf("*")).equals("*extends")) {
                    System.out.println("existingNodesClasses"+existingNode.get().toString());
                    if (relations.size() > 0) {
                        for (String interfaceName : relations) {
                            Optional<MutableNode> existingInterface = abstractClassGraph.nodes().stream()
                                    .filter(n -> n.name().equals(interfaceName))
                                    .findFirst();

                            if (!existingInterface.isPresent())
                            {
                                MutableNode interfaceNode = mutNode(interfaceName).add(Shape.RECTANGLE).add(Label.of(interfaceName + "\nbase class"));
                                abstractClassGraph.add(interfaceNode);
                                abstractClassGraph.addLink(existingNode.get(), interfaceNode);

                            }else{
                                abstractClassGraph.addLink(existingNode.get(), existingInterface.get());
                            }
                        }
                    }
                }
            }
        }
        graph.add(abstractClassGraph);

        outToPNG(abstractClassGraph);
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
            static Map<String, List<String>> classes;

            public ClassVisitor(){
              classes = new HashMap<String, List<String>>();
            }
            public Map<String, List<String>> getClasses() {
                return classes;
            }

            @Override
            public void visit(ClassOrInterfaceDeclaration cls, Void arg) {
                super.visit(cls, arg);
                try {
                    analyzeRelations(cls);
                } catch (UnsolvedSymbolException e) {
                    // Handle unsolved symbol exception
                    System.err.println("UnsolvedSymbolException occurred: " + e.getMessage());
                }
            }

            private void analyzeRelations(ClassOrInterfaceDeclaration cls) {
                analyzeExtendTypes(cls);
                analyzeImplementedTypes(cls);
            }

            private void analyzeImplementedTypes(ClassOrInterfaceDeclaration cls) {
                List<String>implementedTypes = new ArrayList<>();
                for (ClassOrInterfaceType implementedType : cls.getImplementedTypes()) {
                    String relation = implementedType.toString();
                    implementedTypes.add(relation);
                }
                classes.put(cls.getNameAsString()+"*implemented", implementedTypes);
            }

            private void analyzeExtendTypes(ClassOrInterfaceDeclaration cls) {
                List<String>extendedTypes = new ArrayList<>();
                for (ClassOrInterfaceType extendsType : cls.getExtendedTypes()) {
                    String relation = extendsType.toString();
                    extendedTypes.add(relation);
                }
                classes.put(cls.getNameAsString()+"*extends", extendedTypes);
            }

        }
}
