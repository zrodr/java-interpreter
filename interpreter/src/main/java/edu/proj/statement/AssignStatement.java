package edu.proj.statement;

import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;

public class AssignStatement extends Statement{
    private final String varName;
    private final Expression exp;

    public AssignStatement(String variable, Expression expression) {
        this.varName = variable;
        this.exp = expression;
    }

    @Override
    public void run(ProgramState programState) {
        programState.setVariable(varName, exp.evaluate(programState));
    }
}
