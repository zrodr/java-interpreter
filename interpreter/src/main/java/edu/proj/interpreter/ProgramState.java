package edu.proj.interpreter;

import edu.proj.statement.Statement;

import java.util.*;
/**
 * A class which tracks the state of a running program. ProgramState should maintain a call stack, with each call frame
 * tracking variable names and their corresponding values. ProgramState should also keep track of function definitions,
 * so that when function calls are made, they can be run and evaluated.
 */
public class ProgramState {
    private final Stack< Map<String, Integer> > callStack = new Stack<>();

    //getter function for test purposes
    public int getStackSize(){
        if(callStack.isEmpty()){
            return 0;
        }
        return callStack.size();
    }

    public ProgramState() {
        addNewCallFrame();
    }

    public int getVariable(String variable) {
        if(!callStack.isEmpty() && callStack.peek().containsKey(variable)) {
            return callStack.peek().get(variable);
        }
        return -1;
    }

    public void setVariable(String variable, int value) {
        if(!callStack.isEmpty()) {
            callStack.peek().put(variable, value);
        }
    }

    public void addNewCallFrame() {
        Map<String, Integer> callFrame = new HashMap<>();
        callStack.push(callFrame);
    }

    public void removeCurrentCallFrame() {
        if(!callStack.isEmpty()) {
            callStack.pop();
        }
    }

    //members/functions for function parameters

    private final Map<String, List<String>> functionParams = new HashMap<>();

    public void setFunctionParams(String functionName, List<String> params){
        List<String> parameterNames = new ArrayList<>(params);
        functionParams.put(functionName, parameterNames);
    }

    public List<String> getFunctionParams(String functionName){
        if(!functionParams.isEmpty() && functionParams.containsKey(functionName)){
            return functionParams.get(functionName);
        }
        return null;
    }

    // members/functions for function statements

    private final Map<String, List<Statement>> functionStatements = new HashMap<>();
    private Integer returnValue;

    public void setFunctionStatements(String functionName, List<Statement> functionBody){
        List<Statement> body = new ArrayList<>(functionBody);
        functionStatements.put(functionName, body);
    }

    public List<Statement> getFunctionStatements(String functionName){
        if(!functionStatements.isEmpty() && functionStatements.containsKey(functionName)){
            return functionStatements.get(functionName);
        }
        return null;
    }

    public boolean hasReturnValue() {
        if(returnValue != null){
            return true;
        }
        return false;
    }

    public int getReturnValue() {
        if(returnValue != null){
            return returnValue.intValue();
        }
        return -1;
    }

    public void setReturnValue(int value) {
        returnValue = Integer.valueOf(value);
    }

    public void clearReturnValue() {
        returnValue = null;
    }
}
