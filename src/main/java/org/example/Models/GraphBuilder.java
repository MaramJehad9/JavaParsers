package org.example.Models;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.util.List;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class GraphBuilder {
    // Other methods remain the same
    private MutableGraph projectGraph;
    public MutableGraph getProjectGraph() {
        return projectGraph;
    }
    public GraphBuilder(MutableGraph projectGraph) {
        this.projectGraph = projectGraph;
    }
    public void addNodeToGraph(ClassOrInterfaceDeclaration cls, List<MethodDeclaration> methodsDec, List<FieldDeclaration> fields) {
        MutableNode classNode = mutNode(cls.getNameAsString());

        // Add methods as child nodes
        for (MethodDeclaration methodDec : methodsDec) {
            String methodName = methodDec.getNameAsString();
            MutableNode methodNode = mutNode(methodName);
            classNode.addLink(methodNode);
        }

        // Add fields as child nodes
        for (FieldDeclaration field : fields) {
            String fieldName = field.getVariable(0).getNameAsString();
            MutableNode fieldNode = mutNode(fieldName);
            classNode.addLink(fieldNode);
        }

        // Add links for inheritance (extend)
        if (cls.getExtendedTypes().isNonEmpty()) {
            String parentClassName = cls.getExtendedTypes().get(0).getNameAsString();
            MutableNode parentNode = mutNode(parentClassName);
            classNode.addLink(parentNode);
        }

        // Add links for composition (fields and parameters)
        for (FieldDeclaration field : fields) {
            // Handle composition relationships
            // Example: if field type is another class, create a link
        }

        projectGraph.add(classNode);
    }
}
