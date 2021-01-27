package expression;

import edu.proj.expression.Expression;
import edu.proj.expression.FunctionExpression;
import edu.proj.interpreter.ProgramState;
import edu.proj.statement.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FunctionExpressionTest {
    private final ProgramState state = new ProgramState();
    @Test
    public void instantiation(){
        Expression exp = Expression.create("func(3, 5)");
        assertThat(exp instanceof FunctionExpression, equalTo(true));
    }

    @Test
    public void executeSimpleFunction(){
        List<String> params = new ArrayList<>();
        params.add("a");
        params.add("b");
        List<Statement> body = new ArrayList<>();
        Expression bodyExp = Expression.create("a + b");
        body.add(new ReturnStatement(bodyExp));

        DefineFunctionStatement st = new DefineFunctionStatement("function", params, body);
        st.run(state);

        Expression exp = Expression.create("function(3, 5)");

        assertThat(exp.evaluate(state), equalTo(8));
    }

    @Test
    public void executeMultiLineFunction(){
        List<String> params = new ArrayList<>();
        List<Statement> body = new ArrayList<>();
        body.add(new PrintStatement(Expression.create("1")));
        body.add(new PrintStatement(Expression.create("55")));
        body.add(new ReturnStatement(Expression.create("1 + 55")));

        DefineFunctionStatement st = new DefineFunctionStatement("test", params, body);
        st.run(state);

        Expression exp = Expression.create("test()");

        assertThat(exp.evaluate(state), equalTo(56));
    }
}
