package org.example.Models;
import java.lang.reflect.Method;
import java.util.*;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.analysis.ControlFlow;
import javassist.bytecode.analysis.ControlFlow.Block;
import javassist.bytecode.analysis.ControlFlow.Node;
import javassist.CtMethod;


public class CyclomaticComplexityCalculator {
    private ClassOrInterfaceDeclaration classDeclaration;
    private Map<String, Integer> complexityForMethod;

    public CyclomaticComplexityCalculator(ClassOrInterfaceDeclaration classDeclaration) {
        this.classDeclaration = classDeclaration;
        this.complexityForMethod = new HashMap<>();
    }
    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }
    public Map<String, Integer> getComplexityForMethod() {
        return complexityForMethod;
    }
    public Map<String, Integer> getComplexityForMethods() {
        this.classDeclaration.getMethods().forEach(methodDeclaration -> {
            // Convert MethodDeclaration to CtMethod
            CtMethod ctMethod = getCtMethod(methodDeclaration);
            if (ctMethod != null) {
                complexityForMethod.put(ctMethod.getName(), CalculateCCForMethod(ctMethod));
            }
        });
        return complexityForMethod;
    }

    // Method to convert MethodDeclaration to CtMethod
    private CtMethod getCtMethod(MethodDeclaration methodDeclaration) {
        try {
            // Get the fully qualified name of the class containing the method
            String fileName = methodDeclaration.findCompilationUnit().get().getStorage().get().getFileName().toString();
            String className = fileName.substring(0, fileName.lastIndexOf(".java"));

            ClassPool classPool = ClassPool.getDefault();
            classPool.insertClassPath(new ClassClassPath(Class.forName(className)));

            CtClass ctClass = classPool.get(className);
            // Get the method name and parameters
            String methodName = methodDeclaration.getNameAsString();
            String methodParams = methodDeclaration.getParameters().toString();
            // Find the method in the class
            CtMethod[] ctMethods = ctClass.getDeclaredMethods(methodName);
            for (CtMethod ctMethod : ctMethods) {
                // Check if the method parameters match
                if (ctMethod.getSignature().equals(methodParams)) {
                    return ctMethod;
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
            return null; // Error occurred during calculation
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private int CalculateCCForMethod(CtMethod method) {
        try {
            // Use Javassist library to analyze bytecode and calculate cyclomatic complexity
            ControlFlow controlFlow = new ControlFlow(method);

            // Get the number of basic blocks (nodes) in the method
            int numNodes = controlFlow.basicBlocks().length;

            // Get the number of edges (branches) in the method
            int numEdges = 0;
            for (ControlFlow.Block block : controlFlow.basicBlocks()) {
                int exits = block.exits();
                numEdges += exits;
            }

            // Cyclomatic complexity formula: E - N + 2P
            int cyclomaticComplexity = numEdges - numNodes + 2;

            return cyclomaticComplexity;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error occurred during calculation
        }

    }
}
