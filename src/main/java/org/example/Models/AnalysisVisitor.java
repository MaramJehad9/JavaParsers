package org.example.Models;

import com.github.javaparser.Position;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

public class AnalysisVisitor extends VoidVisitorAdapter<Void> {
    private static Map<String, ClassInfo> classInfoMap;
    public AnalysisVisitor(){
        this.classInfoMap = new HashMap<>();
    }
    public static Map<String, ClassInfo> getClassInfoMap() {
        return classInfoMap;
    }


    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        super.visit(classDeclaration, arg);

        ClassInfo classInfo = getDetailedClassInfo(classDeclaration);
        String className = classInfo.getClassName();

        MethodCallVisitor methodCallVisitor = new MethodCallVisitor(className);
        classDeclaration.accept(methodCallVisitor, null);

        MethodsVisitor methodsVisitor = new MethodsVisitor();
        classDeclaration.accept(methodsVisitor, null);

        classInfo.setMethodInvocations(methodCallVisitor.getMethodInvocations());
        classInfo.setMethodInfos(methodsVisitor.getMethodsList());

        classInfoMap.put(className, classInfo);
    }
    private ClassInfo getDetailedClassInfo(ClassOrInterfaceDeclaration classDeclaration) {

            String className = classDeclaration.getNameAsString();
            Set<String> extendedClasses = new HashSet<>();
            Set<String> implementClasses = new HashSet<>();
            List<String> fields = new ArrayList<>();
            int numOfMethods = classDeclaration.getMethods().size();
            int numOfAttributes = classDeclaration.getFields().size();
            int numOfConstructors = classDeclaration.getConstructors().size();
            int numOfChildClasses = classDeclaration.getChildNodes().size();
            Position defaultBeginPosition = new Position(0, 0);
            Position defaultEndPosition = new Position(0, 0);
            List<String> implementInterfaces = new ArrayList<String>();

            classDeclaration.getExtendedTypes().forEach(type -> extendedClasses.add(type.getNameAsString()));
            classDeclaration.getImplementedTypes().forEach(type -> implementClasses.add(type.getNameAsString()));

            classDeclaration.getFields().forEach(fieldDeclaration -> {
                    fieldDeclaration.getVariables().forEach(variableDeclarator -> {
                        fields.add(variableDeclarator.getNameAsString());
                    });
                });

            Position beginPosition = classDeclaration.getBegin().orElse(defaultBeginPosition);
            Position endPosition = classDeclaration.getEnd().orElse(defaultEndPosition);
            System.out.println(classDeclaration.getChildNodes().getClass().getName());
            classDeclaration.getImplementedTypes().forEach(implementInterface -> implementInterfaces.add(implementInterface.getNameAsString()));

            // Calculate the number of lines
            int numOfLines = endPosition.line - beginPosition.line;
            ClassInfo classInfo = new ClassInfo(className, extendedClasses, implementClasses,  numOfConstructors,  numOfChildClasses,  numOfLines,  numOfAttributes,  numOfMethods, fields);

            return classInfo;

    }
}
