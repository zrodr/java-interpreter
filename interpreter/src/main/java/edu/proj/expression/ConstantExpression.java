package edu.proj.expression;

import edu.proj.interpreter.ProgramState;


public class ConstantExpression extends Expression{
    private final int val;

    public ConstantExpression(int value){
        this.val = value;
    }

    @Override
    public int evaluate(ProgramState programState) {
        return val;
    }
}
