package statement;

import edu.proj.expression.Condition;
import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;
import edu.proj.statement.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class IfStatementTest {
    private final ProgramState state = new ProgramState();
    @Test
    public void instantiation(){
        Condition cond = Condition.create("1 == 1");
        List<Statement> body = new ArrayList<>();

        IfStatement st = new IfStatement(cond, body);

        assertThat(st instanceof BlockStatement, equalTo(true));
    }

    @Test
    public void falseCondition(){
        Condition cond = Condition.create("1 == 0");
        List<Statement> body = new ArrayList<>();
        body.add(new AssignStatement("var", Expression.create("12 + 60")));

        IfStatement st = new IfStatement(cond, body);
        st.run(state);
        //if block will not run so value is not found
        assertThat(state.getVariable("var"), equalTo(-1));
    }

    @Test
    public void trueCondition(){
        Condition cond = Condition.create("1 == 1");
        List<Statement> body = new ArrayList<>();
        body.add(new AssignStatement("var", Expression.create("12 + 60")));

        IfStatement st = new IfStatement(cond, body);
        st.run(state);

        assertThat(state.getVariable("var"), equalTo(72));
    }

    @Test
    public void returnFromIfStatement(){
        Condition cond = Condition.create("1 == 1");
        List<Statement> body = new ArrayList<>();
        body.add(new AssignStatement("x", Expression.create("10")));
        body.add(new ReturnStatement(Expression.create("x")));
        body.add(new AssignStatement("x", Expression.create("15")));

        IfStatement st = new IfStatement(cond, body);
        st.run(state);
        //if block should exit before x is reassigned
        assertThat(state.getVariable("x"), not(15));
    }

    @Test
    public void nestedIf(){
        Condition cond = Condition.create("1 == 1");
        List<Statement> body = new ArrayList<>();
        body.add(new AssignStatement("y", Expression.create("2 + 2")));
        List<Statement> nestedBody = new ArrayList<>();
        nestedBody.add(new AssignStatement("y", Expression.create("4 + 5")));
        body.add(new IfStatement(Condition.create("1 < 0"), nestedBody));

        IfStatement st = new IfStatement(cond, body);

        st.run(state);

        assertThat(state.getVariable("y"), equalTo(4));
    }
}
