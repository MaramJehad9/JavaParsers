package org.example.Models;
public class Module {
    private String name;
    private int inDataParams;
    private int inControlParams;
    private int outDataParams;
    private int outControlParams;
    private int localDataVars;
    private int localControlVars;
    private int globalDataVars;
    private int globalControlVars;
    private int numModulesCalled;
    private double DataFlowCohesion;
    private double functionalCohesion;

    public Module() {

    }

    public double getFunctionalCohesion() {
        return functionalCohesion;
    }

    public void setFunctionalCohesion(double functionalCohesion) {
        this.functionalCohesion = functionalCohesion;
    }

    public void setDataFlowCohesion(double dataFlowCohesion) {
        DataFlowCohesion = dataFlowCohesion;
    }

    public double getDataFlowCohesion() {
        return DataFlowCohesion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInDataParams(int inDataParams) {
        this.inDataParams = inDataParams;
    }

    public void setInControlParams(int inControlParams) {
        this.inControlParams = inControlParams;
    }

    public void setOutDataParams(int outDataParams) {
        this.outDataParams = outDataParams;
    }

    public void setOutControlParams(int outControlParams) {
        this.outControlParams = outControlParams;
    }

    public void setLocalDataVars(int localDataVars) {
        this.localDataVars = localDataVars;
    }

    public void setLocalControlVars(int localControlVars) {
        this.localControlVars = localControlVars;
    }

    public void setGlobalDataVars(int globalDataVars) {
        this.globalDataVars = globalDataVars;
    }

    public void setGlobalControlVars(int globalControlVars) {
        this.globalControlVars = globalControlVars;
    }

    public void setNumModulesCalled(int numModulesCalled) {
        this.numModulesCalled = numModulesCalled;
    }

    public Module(String name, int inDataParams, int inControlParams, int outDataParams, int outControlParams, int localDataVars, int localControlVars, int globalDataVars, int globalControlVars, int numModulesCalled) {
        this.name = name;
        this.inDataParams = inDataParams;
        this.inControlParams = inControlParams;
        this.outDataParams = outDataParams;
        this.outControlParams = outControlParams;
        this.localDataVars = localDataVars;
        this.localControlVars = localControlVars;
        this.globalDataVars = globalDataVars;
        this.globalControlVars = globalControlVars;
        this.numModulesCalled = numModulesCalled;
    }

    public String getName() {
        return name;
    }

    public int getInDataParams() {
        return inDataParams;
    }

    public int getInControlParams() {
        return inControlParams;
    }

    public int getOutDataParams() {
        return outDataParams;
    }

    public int getOutControlParams() {
        return outControlParams;
    }

    public int getLocalDataVars() {
        return localDataVars;
    }

    public int getLocalControlVars() {
        return localControlVars;
    }

    public int getGlobalDataVars() {
        return globalDataVars;
    }

    public int getGlobalControlVars() {
        return globalControlVars;
    }

    public int getNumModulesCalled() {
        return numModulesCalled;
    }
    // Constructor, getters, and setters

    public double calculateFunctionalCohesion() {
        // Implement functional cohesion calculation based on the provided formula
        double p = inDataParams + 2 * inControlParams + outDataParams + 2 * outControlParams
                + localDataVars + 2 * localControlVars + globalDataVars + 2 * globalControlVars + 2 * numModulesCalled;
        double cohesion = 1 / Math.max(p, 1);
        return cohesion;
    }

    public double calculateDataFlowCohesion() {
        // Calculate data flow cohesion based on the number of data dependencies between statements or methods
        // This calculation may involve analyzing the flow of data between different parts of the module
        // You can consider various factors such as variable dependencies, method invocations, etc.

        // For simplicity, let's calculate a hypothetical data flow cohesion value

        // Calculate the total number of data dependencies
        int totalDataDependencies = inDataParams + outDataParams + localDataVars + globalDataVars;

        // Calculate the total number of statements or methods in the module
        int totalStatements = inDataParams + inControlParams + outDataParams + outControlParams
                + localDataVars + localControlVars + globalDataVars + globalControlVars
                + numModulesCalled; // Adjust this as per your project logic

        // Calculate the data flow cohesion as a ratio of data dependencies to total statements
        double dataFlowCohesion = (double) totalDataDependencies / totalStatements;

        return dataFlowCohesion;
    }

    public void incrementInDataParams() {
        this.inDataParams++;
    }

    public void incrementOutDataParams() {
        this.outDataParams++;
    }

    public void incrementLocalDataVars() {
        this.localDataVars++;
    }

    public void incrementGlobalDataVars() {
        this.globalDataVars++;
    }

    public void incrementNumModulesCalled() {
        this.numModulesCalled++;
    }


}
