package edu.proj.statement;

import edu.proj.interpreter.ProgramState;

import java.util.List;

public class DefineFunctionStatement extends Statement{
    private final String functionName;
    private final List<String> params;
    private final List<Statement> functionBody;

    public DefineFunctionStatement(String functionName, List<String> params, List<Statement> body){
        this.functionName = functionName;
        this.params = params;
        this.functionBody = body;
    }

    @Override
    public void run(ProgramState programState) {
        programState.setFunctionParams(functionName, params);
        programState.setFunctionStatements(functionName, functionBody);
    }
}
