package org.example.Controller;

import static guru.nidi.graphviz.model.Factory.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class DirectoryGraphGenerator {
    public static void main(String[] args) throws IOException {
        // Directory containing Java source files
        String directoryPath = "C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\src\\main\\java\\org\\jhotdraw\\app\\";


        // Create a new mutable graph
        MutableGraph graph = mutGraph("classGraph").setDirected(true);

        // Traverse files in the directory
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        CompilationUnit cu = StaticJavaParser.parse(path);

                        // Visit each class or interface declaration in the AST
                        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
                            String className = cls.getNameAsString();

                            // Create a mutable node for the class
                            MutableNode classNode = mutNode(className);

                            // Add the class node to the graph
                            graph.add(classNode);

                            // Add relations between classes (e.g., superclass, implemented interfaces)
                            if (cls.getExtendedTypes().isNonEmpty()) {
                                // Get the superclass name
                                String superClass = cls.getExtendedTypes().get(0).getNameAsString();

                                // Create a node for the superclass
                                MutableNode superNode = mutNode(superClass);

                                // Add a link from the class node to the superclass node with a label
                                graph.add(classNode.addLink(superNode).add(Label.of(superClass)).add(Style.DASHED));
                            }

                            // Add interface implementation relationships
                            cls.getImplementedTypes().forEach(i -> {
                                // Get the interface name
                                String interfaceName = i.getNameAsString();

                                // Create a node for the interface
                                MutableNode interfaceNode = mutNode(interfaceName);

                                // Add a link from the class node to the interface node with a label
                                graph.add(classNode.addLink(interfaceNode).add(Label.of(interfaceName)).add(Style.SOLID));
                            });
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        // Render the graph
        Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(directoryPath + "/classGraph2.png"));
    }
}
