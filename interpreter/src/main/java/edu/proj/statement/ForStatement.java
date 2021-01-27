package edu.proj.statement;

import edu.proj.expression.Condition;
import edu.proj.interpreter.ProgramState;

import java.util.List;

public class ForStatement extends LoopStatement{
    private final Statement initialization;
    private final Statement update;

    public ForStatement(Statement init, Condition cond, Statement update, List<Statement> loopBody){
        super(cond, loopBody);
        this.initialization = init;
        this.update = update;
    }

    @Override
    protected void runInitialization(ProgramState programState) {
        initialization.run(programState);
    }

    @Override
    protected void runUpdate(ProgramState programState) {
        update.run(programState);
    }
}