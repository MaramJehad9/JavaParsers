package org.example.Models;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodInfo {
    String MethodName;
    String MethodType;
    int MethodCyclomaticComplexity;
    int MethodLinesOfCode;
    int MethodNumberOfParameters;

    Set<String> InvokedBy;
    String MemberOf;
    public MethodInfo() {
         InvokedBy = new HashSet<>();
    }

    public Set<String> getInvokedBy() {
        return InvokedBy;
    }

    public void setInvokedBy(Set<String> invokedBy) {
        InvokedBy = invokedBy;
    }

    public void addToInvokedBy(String invokedBy) {
        InvokedBy.add(invokedBy);
    }
    public String getMemberOf() {
        return MemberOf;
    }

    public void setMemberOf(String memberOf) {
        MemberOf = memberOf;
    }

    public MethodInfo(String methodName, String methodType, int methodCyclomaticComplexity, int methodLinesOfCode, int methodNumberOfParameters) {
        MethodName = methodName;
        MethodType = methodType;
        MethodCyclomaticComplexity = methodCyclomaticComplexity;
        MethodLinesOfCode = methodLinesOfCode;
        MethodNumberOfParameters = methodNumberOfParameters;
    }

    public void setMethodName(String methodName) {
        MethodName = methodName;
    }

    public void setMethodType(String methodType) {
        MethodType = methodType;
    }

    public void setMethodCyclomaticComplexity(int methodCyclomaticComplexity) {
        MethodCyclomaticComplexity = methodCyclomaticComplexity;
    }

    public void setMethodLinesOfCode(int methodLinesOfCode) {
        MethodLinesOfCode = methodLinesOfCode;
    }

    public void setMethodNumberOfParameters(int methodNumberOfParameters) {
        MethodNumberOfParameters = methodNumberOfParameters;
    }

    public String getMethodName() {
        return MethodName;
    }

    public String getMethodType() {
        return MethodType;
    }

    public int getMethodCyclomaticComplexity() {
        return MethodCyclomaticComplexity;
    }

    public int getMethodLinesOfCode() {
        return MethodLinesOfCode;
    }

    public int getMethodNumberOfParameters() {
        return MethodNumberOfParameters;
    }

    public void reset(){
        this.InvokedBy = new HashSet<>();
        this.MemberOf = null;
    }
}
