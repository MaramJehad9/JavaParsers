package org.example.Models;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import java.util.HashSet;
import java.util.Set;

public class DependencyAnalyzer {

    private Set<String> getExternalDependencies(CtClass clazz) throws NotFoundException {
        Set<String> externalDependencies = new HashSet<>();

        // Get all declared methods
        for (CtMethod method : clazz.getDeclaredMethods()) {
            CtClass[] parameterTypes = method.getParameterTypes();
            for (CtClass parameterType : parameterTypes) {
                if (!parameterType.isPrimitive() && !parameterType.getName().startsWith("java.")) {
                    externalDependencies.add(parameterType.getName());
                }
            }
            CtClass returnType = method.getReturnType();
            if (!returnType.isPrimitive() && !returnType.getName().startsWith("java.")) {
                externalDependencies.add(returnType.getName());
            }
        }

        // Get the superclass
        CtClass superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.getName().startsWith("java.")) {
            externalDependencies.add(superClass.getName());
        }

        // Get the implemented interfaces
        CtClass[] interfaces = clazz.getInterfaces();
        for (CtClass interfaceType : interfaces) {
            if (!interfaceType.getName().startsWith("java.")) {
                externalDependencies.add(interfaceType.getName());
            }
        }

        return externalDependencies;
    }
}
