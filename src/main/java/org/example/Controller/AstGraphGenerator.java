package org.example.Controller;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class AstGraphGenerator {
    private static MutableGraph projectGraph;

    public static void main(String[] args) throws IOException, ParseException {
        String directoryPath = "C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\src\\main\\java\\org\\jhotdraw\\app\\";

        projectGraph = guru.nidi.graphviz.model.Factory.mutGraph("Project AST").setDirected(true);

        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        CompilationUnit cu = StaticJavaParser.parse(path.toFile());
                        cu.accept(new ClassVisitor(), null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        Graphviz.fromGraph(projectGraph).render(Format.PNG).toFile(new File(directoryPath + "project_ast_graph_1.png"));
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration cls, Void arg) {
            super.visit(cls, arg);

            String fileName = new File(cls.getFullyQualifiedName().orElse("Unknown")).getName();
            MutableNode fileNode = mutNode(fileName);
            projectGraph.add(fileNode);
            System.out.println(fileName);
            cls.findAll(MethodDeclaration.class).forEach(method -> {
                MutableNode methodNode = mutNode(method.getNameAsString());
                projectGraph.add(methodNode);
                projectGraph.add(fileNode.addLink(methodNode));

                method.accept(new MethodVisitor(methodNode), null);
            });
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        private final MutableNode sourceMethodNode;

        public MethodVisitor(MutableNode sourceMethodNode) {
            this.sourceMethodNode = sourceMethodNode;
        }

        @Override
        public void visit(MethodCallExpr methodCall, Void arg) {
            super.visit(methodCall, arg);

            String methodName = methodCall.getNameAsString();
            String declaringClassName = methodCall.getScope().map(expr -> expr.toString()).orElse(null);

            MutableNode targetClassNode = mutNode(declaringClassName);
            projectGraph.add(targetClassNode);
            System.out.println(declaringClassName);
            if (methodName != null) {
                projectGraph.add(sourceMethodNode.addLink(targetClassNode).add(Label.of(methodName)));
            }
        }
    }
}
