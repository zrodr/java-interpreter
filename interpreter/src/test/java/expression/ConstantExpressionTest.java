package expression;

import edu.proj.expression.ConstantExpression;
import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ConstantExpressionTest {
    private final ProgramState state = new ProgramState();
    @Test
    public void instantiation(){
        Expression exp = Expression.create("6");
        assertThat(exp instanceof ConstantExpression, equalTo(true));
    }

    @Test
    public void retrieveValue(){
        Expression exp = Expression.create("43");
        assertThat(exp.evaluate(state), equalTo(43));
    }

    @Test
    public void assignNegValue(){
        Expression exp = Expression.create("-68");
        assertThat(exp.evaluate(state), equalTo(-68));
    }

    @Test
    public void assignLargeValue(){
        Expression exp = Expression.create("2147483646"); // max size of signed integer - 1
        assertThat(exp.evaluate(state), equalTo(2147483646));
    }

    @Test
    public void invalidConstantExp(){
        Expression exp = Expression.create("two");
        assertThat(exp.evaluate(state), equalTo(-1));
    }
}
