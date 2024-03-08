package org.example.Models;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class GraphVisualization {
    private final MutableGraph projectGraph;

    public GraphVisualization(MutableGraph graph) {
        projectGraph = graph;
    }

    public static void visit() throws IOException {
        // Create a new graph
        MutableGraph graph = guru.nidi.graphviz.model.Factory.mutGraph("Method Invocations").setDirected(true);

        // Add nodes and edges based on the collected data
        addNodesAndEdges(graph);
        String path = "C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\src\\main\\java\\org\\jhotdraw\\app\\";
        // Render the graph to a PNG image file
        Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(path + "method_invocations_graph.png"));
    }

    private static void addNodesAndEdges(MutableGraph graph) {
        // Add nodes for classes
        MutableNode classA = mutNode("ClassA");
        MutableNode classB = mutNode("ClassB");

        // Add method invocations as edges
        classA.addLink(classB).add(Label.of("method1"));
        classB.addLink(classA).add(Label.of("method2"));

        // Add nodes and edges to the graph
        graph.add(classA, classB);
    }


}
