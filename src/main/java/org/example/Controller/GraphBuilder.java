package org.example.Controller;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.LinkAttr;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.*;

public class GraphBuilder {
    private MutableGraph projectGraph;

    public GraphBuilder(MutableGraph projectGraph) {
        this.projectGraph = projectGraph;
    }

    public void addNodeToGraph(ClassOrInterfaceDeclaration cls) {
        MutableNode classNode = mutNode(cls.getNameAsString()).add(Shape.RECTANGLE);
        projectGraph.add(classNode);

        for (ClassOrInterfaceType extendedType : cls.getExtendedTypes()) {
            String relation = extendedType.getNameAsString();
            MutableNode parentNode = mutNode(relation).add(Shape.RECTANGLE);
            projectGraph.add(parentNode);
            classNode.addLink(parentNode).add(Label.of(relation)).add(Style.BOLD); // Use bold font for extends
        }

        for (ClassOrInterfaceType implementedType : cls.getImplementedTypes()) {
            String relation = implementedType.getNameAsString();
            MutableNode parentNode = mutNode(relation).add(Shape.HEXAGON);
            projectGraph.add(parentNode);
            classNode.addLink(parentNode).add(Label.of(relation)).add(Style.DOTTED); // Use italic font for implements
        }


    }

    public MutableGraph getProjectGraph() {
        return projectGraph;
    }

    public void printGraphToFile(String filePath) {
        try {
            Graphviz.fromGraph(projectGraph).render(Format.PNG).toFile(new File(filePath));
            System.out.println("Graph has been printed to: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to print the graph: " + e.getMessage());
        }
    }
}
