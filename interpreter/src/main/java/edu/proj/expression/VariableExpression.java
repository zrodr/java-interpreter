package edu.proj.expression;

import edu.proj.interpreter.ProgramState;


public class VariableExpression extends Expression{
    private final String variable;

    public VariableExpression(String varName){
        this.variable = varName;
    }

    @Override
    public int evaluate(ProgramState programState) {
        return programState.getVariable(variable);
    }
}
