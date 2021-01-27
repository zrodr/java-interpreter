package edu.proj.statement;

import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;

public class ReturnStatement extends Statement{
    private final Expression returnExpression;

    public ReturnStatement(Expression returnExpression){
        this.returnExpression = returnExpression;
    }
    @Override
    public void run(ProgramState programState) {
        programState.setReturnValue(returnExpression.evaluate(programState));
    }
}
