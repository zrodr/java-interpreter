package edu.proj.statement;

import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;

public class PrintStatement extends Statement {
    private final Expression exp;

    public PrintStatement(Expression expression){
        this.exp = expression;
    }

    @Override
    public void run(ProgramState programState) {
        System.out.println(exp.evaluate(programState));
    }
}
