package org.example.Models;

import java.util.List;
import java.util.Map;

public class MethodsAnalysis {
    String ClassName;
    Map <String,Integer> methodsCCMap;


    public MethodsAnalysis(String className, Map<String, Integer> methodsCCMap) {
        ClassName = className;
        this.methodsCCMap = methodsCCMap;
    }

    public String getClassName() {
        return ClassName;
    }

    public Map<String, Integer> getMethodsCCMap() {
        return methodsCCMap;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public void setMethodsCCMap(Map<String, Integer> methodsCCMap) {
        this.methodsCCMap = methodsCCMap;
    }
}
