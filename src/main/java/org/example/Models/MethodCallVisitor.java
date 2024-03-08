package org.example.Models;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MethodCallVisitor extends VoidVisitorAdapter<Void> {
    private String className;
    private Map<String, Set<String>> methodInvocations;

    public Map<String, Set<String>> getMethodInvocations() {
        return methodInvocations;
    }

    public MethodCallVisitor(String className) {
        this.className = className;
        this.methodInvocations = new HashMap<>();
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