package org.example.Models;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodsVisitor extends VoidVisitorAdapter<Void> {
        private static List<MethodInfo> MethodsMap;
        public MethodsVisitor() {
            MethodsMap = new ArrayList<>();
        }

        @Override
        public void visit(MethodDeclaration n, Void arg) {
            // Calculate Cyclomatic Complexity for the current method
            int cc = calculateMethodsCyclomaticComplexity(n);
            String methodName = n.getNameAsString();
            MethodInfo methodInfo = new MethodInfo(methodName, n.getTypeAsString(), cc, n.getBegin().get().line, n.getParameters().size());
            MethodsMap.add(methodInfo);
            // Continue visiting other nodes
            super.visit(n, arg);
        }

        private int calculateMethodsCyclomaticComplexity(MethodDeclaration method) {
            int complexity = 1; // Start with 1 for the method itself
            BlockStmt body = method.getBody().orElse(null);

            if (body != null) {
                // Count the number of control flow statements
                for (Statement statement : body.getStatements()) {
                    if (statement.isIfStmt() || statement.isSwitchStmt() || statement.isWhileStmt() ||
                            statement.isDoStmt() || statement.isForStmt() || statement.isForEachStmt() ||
                            statement.isTryStmt()) {
                        complexity++;
                    }
                    // Additional complexity for each catch clause
                    else if (statement.isTryStmt()) {
                        TryStmt tryStmt = statement.asTryStmt();
                        complexity += tryStmt.getCatchClauses().size();
                    }
                }
            }

            return complexity;
        }

    public static List<MethodInfo> getMethodsList() {
        return MethodsMap;
    }
}