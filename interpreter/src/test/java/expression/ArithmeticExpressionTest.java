package expression;

import edu.proj.expression.ArithmeticExpression;
import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ArithmeticExpressionTest {
    private final ProgramState state = new ProgramState();
    @Test
    public void instantiation(){
        Expression exp = Expression.create("16+87/12");
        assertThat(exp instanceof ArithmeticExpression, equalTo(true));
    }

    @Test
    public void simpleAddition_Subtraction(){
        Expression exp = Expression.create("2+2");
        Expression exp2 = Expression.create("17-8");
        assertThat(exp.evaluate(state), equalTo(4));
        assertThat(exp2.evaluate(state), equalTo(9));
    }

    @Test
    public void simpleMultiplication_Division(){
        Expression exp = Expression.create("18*5");
        Expression exp2 = Expression.create("56/8");
        assertThat(exp.evaluate(state), equalTo(90));
        assertThat(exp2.evaluate(state), equalTo(7));
    }

    @Test
    public void mediumLengthExpression(){
        Expression exp = Expression.create("18*5+6/2*3");
        assertThat(exp.evaluate(state), equalTo(99));
    }

    @Test
    public void heftyExpression(){
        Expression exp = Expression.create("6+2/8-3*2+7/6^2");
        assertThat(exp.evaluate(state), equalTo(0)); // true value: 0.44 but integer math rounds to 0.
    }
}
