package edu.proj.statement;

import edu.proj.expression.Condition;
import edu.proj.interpreter.ProgramState;

import java.util.List;

/**
 * An abstract type of BlockStatement which represents a looping statement. Like other BlockStatements, a LoopStatement
 * has a list of Statements representing its body. However, LoopStatements execute by repeatedly running the entire
 * block as long as a condition holds true.
 */
public abstract class LoopStatement extends BlockStatement {
    public LoopStatement(Condition condition, List<Statement> loopBody){
        super(condition, loopBody);
    }

    @Override
    public void run(ProgramState programState) {
        runInitialization(programState);
        while(getCondition().evaluate(programState) && !programState.hasReturnValue()){
            runBlock(programState);
            runUpdate(programState);
        }
    }

    /**
     * An abstract method which runs any initialization step that happens before the loop executes.
     */
    protected abstract void runInitialization(ProgramState programState);

    /**
     * An abstract method which runs any update step that happens after each run of the loop body.
     */
    protected abstract void runUpdate(ProgramState programState);
}
