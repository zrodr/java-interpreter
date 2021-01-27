package edu.proj.statement;

import edu.proj.expression.Condition;
import edu.proj.interpreter.ProgramState;

import java.util.List;

public class WhileStatement extends LoopStatement{
    public WhileStatement(Condition condition, List<Statement> loopBody){
        super(condition, loopBody);
    }

    @Override
    protected void runInitialization(ProgramState programState) {}

    @Override
    protected void runUpdate(ProgramState programState) {}
}
