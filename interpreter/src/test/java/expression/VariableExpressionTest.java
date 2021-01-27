package expression;

import edu.proj.expression.Expression;
import edu.proj.expression.VariableExpression;
import edu.proj.interpreter.ProgramState;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class VariableExpressionTest {
    private final ProgramState state = new ProgramState();

    @Test
    public void instantiation(){
        Expression exp = Expression.create("var");
        assertThat(exp instanceof VariableExpression, equalTo(true));
    }

    @Test
    public void retrieveValue(){
        state.setVariable("X", 78);
        Expression exp = Expression.create("X");
        assertThat(exp.evaluate(state), equalTo(78));
    }

    @Test
    public void retrieveEmptyVariable(){
        Expression exp = Expression.create("X");
        assertThat(exp.evaluate(state), equalTo(-1));
    }

    @Test
    public void sameNameVariableDifferentFrame(){
        state.setVariable("X", 15);
        Expression exp = Expression.create("X");
        state.addNewCallFrame();
        state.setVariable("X", 10);
        Expression exp2 = Expression.create("X");
        assertThat(exp2.evaluate(state), not(15));
    }
}
