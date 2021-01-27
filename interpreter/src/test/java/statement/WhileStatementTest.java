package statement;

import edu.proj.expression.Condition;
import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;
import edu.proj.statement.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class WhileStatementTest {
    private final ProgramState state = new ProgramState();
    @Test
    public void instantiation(){
        Condition cond = Condition.create("1 == 1");
        List<Statement> body = new ArrayList<>();

        WhileStatement st = new WhileStatement(cond, body);

        assertThat(st instanceof BlockStatement, equalTo(true));
        assertThat(st instanceof LoopStatement, equalTo(true));
    }

    @Test
    public void falseCondition(){
        Condition cond = Condition.create("1000 == 10 * 10");
        List<Statement> body = new ArrayList<>();
        body.add(new AssignStatement("z", Expression.create("9")));

        WhileStatement st = new WhileStatement(cond, body);
        st.run(state);

        assertThat(state.getVariable("z"), equalTo(-1));
    }

    @Test
    public void iterateBasedOnVariableValue(){
        state.setVariable("value", 0);
        Condition cond = Condition.create("value <= 10");
        List<Statement> body = new ArrayList<>();
        body.add(new AssignStatement("value", Expression.create("value + 1")));

        WhileStatement st = new WhileStatement(cond, body);
        st.run(state);

        assertThat(state.getVariable("value"), equalTo(11));
    }

    @Test
    public void returnFromLoopEarly(){
        state.setVariable("test", 0);
        Condition cond = Condition.create("test < 5");
        List<Statement> body = new ArrayList<>();
        body.add(new AssignStatement("test", Expression.create("test + 1")));
        body.add(new ReturnStatement(Expression.create("test")));
        body.add(new AssignStatement("test", Expression.create("5")));

        WhileStatement st = new WhileStatement(cond, body);
        st.run(state);

        assertThat(state.getVariable("test"), equalTo(1));
    }

    @Test
    public void nestedLoop(){
        state.setVariable("loop", 0);
        state.setVariable("innerLoop", 0);

        Condition cond = Condition.create("loop < 5");
        List<Statement> body = new ArrayList<>();
        Condition cond2 = Condition.create("innerLoop < 10");
        List<Statement> innerBody = new ArrayList<>();
        innerBody.add(new AssignStatement("innerLoop", Expression.create("innerLoop + 1")));

        body.add(new AssignStatement("loop", Expression.create("loop + 1")));
        body.add(new WhileStatement(cond2, innerBody));

        Statement st = new WhileStatement(cond, body);
        st.run(state);

        assertThat(state.getVariable("loop"), equalTo(5));
        assertThat(state.getVariable("innerLoop"), equalTo(10));
    }
}
