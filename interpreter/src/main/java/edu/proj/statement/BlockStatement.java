package edu.proj.statement;

import edu.proj.expression.Condition;
import edu.proj.interpreter.ProgramState;

import java.util.List;

/**
 * An abstract Statement type that represents a compound Statement, where running the Statement leads to a sequence of
 * other Statements to be run in order.
 */
public abstract class BlockStatement extends Statement {
    private final List<Statement> statements;
    private final Condition condition;

    public BlockStatement(Condition condition, List<Statement> list){
        this.statements = list;
        this.condition = condition;
    }

    protected Condition getCondition() {
        return condition;
    }
    /**
     * Runs every statement in the BlockStatement's block. Note that for certain looping statements, this may be
     * invoked repeatedly.
     */
    protected void runBlock(ProgramState programState) {
        for (Statement statement : statements) {
            statement.run(programState);
            if(programState.hasReturnValue()){
                return;
            }
        }
    }
}
