package org.example.Models;

import java.util.List;
import java.util.Map;
import java.util.Set;

// Create a ClassInfo class to store class-related information
public class ClassInfo {
    private String className;
    private Set<String> extendedClasses, implementClasses;
    private boolean hasComposition;
    private int numOfConstructors, numOfChildClasses, classComplexity, numOfLines, numOfAttributes, numOfMethods;
    private Map<String, Set<String>> methodInvocations;
    private List<MethodInfo> methodInfos;

    private List<String> fields;

    public ClassInfo(int numOfMethods, int numOfAttributes, int numOfLines, Set<String> extendedClasses, Set<String>implementClasses, int numOfConstructors, int numOfChildClasses, Map<String, Set<String>> methodInvocations, List<MethodInfo> methodInfos) {
        this.numOfMethods = numOfMethods;
        this.numOfAttributes = numOfAttributes;
        this.numOfLines = numOfLines;
        this.extendedClasses = extendedClasses;
        this.numOfConstructors = numOfConstructors;
        this.numOfChildClasses = numOfChildClasses;
        this.methodInvocations = methodInvocations;
        this.methodInfos = methodInfos;
        this.implementClasses = implementClasses;
    }

    public ClassInfo(String className, Set<String> extendedClasses, Set<String> implementClasses, int numOfConstructors, int numOfChildClasses, int numOfLines, int numOfAttributes, int numOfMethods, List<String>fields) {
        this.className = className;
        this.extendedClasses = extendedClasses;
        this.implementClasses = implementClasses;
        this.numOfConstructors = numOfConstructors;
        this.numOfChildClasses = numOfChildClasses;
        this.numOfLines = numOfLines;
        this.numOfAttributes = numOfAttributes;
        this.numOfMethods = numOfMethods;
        this.fields = fields;
    }

    public ClassInfo(String className) {
        this.className = className;
    }

    public List<String>getFields(){
        return fields;
    }

    public String getClassName() {
        return className;
    }
    public List<MethodInfo> getMethodInfos() {
        return methodInfos;
    }
    public void setMethodInfos(List<MethodInfo> methodInfos) {
        this.methodInfos = methodInfos;
    }
    public int getClassComplexity() { return classComplexity; }
    public void setClassComplexity(int classComplexity) { this.classComplexity = classComplexity; }
    public int getNumOfMethods() {
        return numOfMethods;
    }
    public int getNumOfAttributes() {
        return numOfAttributes;
    }
    public int getNumOfLines() {
        return numOfLines;
    }
    public Set<String> getExtendedClasses() {
        return extendedClasses;
    }
    public boolean hasComposition() {
        return hasComposition;
    }
    public int getNumOfConstructors() {
        return numOfConstructors;
    }
    public int getNumOfChildClasses() {
        return numOfChildClasses;
    }
    public Map<String, Set<String>> getMethodInvocations() {
        return methodInvocations;
    }
    public void setMethodInvocations(Map<String, Set<String>> methodInvocations) {
        this.methodInvocations = methodInvocations;
    }
    public void setNumOfMethods(int numOfMethods) {
        this.numOfMethods = numOfMethods;
    }
    public void setNumOfAttributes(int numOfAttributes) {
        this.numOfAttributes = numOfAttributes;
    }
    public void setNumOfLines(int numOfLines) {
        this.numOfLines = numOfLines;
    }
    public void setExtendedClasses(Set<String> extendedClasses) {
        this.extendedClasses = extendedClasses;
    }
    public void setHasComposition(boolean hasComposition) {
        this.hasComposition = hasComposition;
    }

    public void setNumOfConstructors(int numOfConstructors) {
        this.numOfConstructors = numOfConstructors;
    }

    public void setNumOfChildClasses(int numOfChildClasses) {
        this.numOfChildClasses = numOfChildClasses;
    }
}

