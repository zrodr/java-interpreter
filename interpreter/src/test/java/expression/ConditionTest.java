package expression;

import edu.proj.expression.Condition;
import edu.proj.interpreter.ProgramState;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ConditionTest {
    private final ProgramState state = new ProgramState();
    @Test
    public void instantiation(){
        Condition cond = Condition.create("54 < 67");
        assertThat(cond instanceof Condition, equalTo(true));
    }

    @Test
    public void Condition_Equals(){
        Condition cond = Condition.create("1 == 0");
        assertThat(cond.evaluate(state), equalTo(false));
    }

    @Test
    public void Condition_NotEquals(){
        Condition cond = Condition.create("1 != 1");
        assertThat(cond.evaluate(state), equalTo(false));
    }

    @Test
    public void Condition_LessThan_GreaterThan(){
        Condition cond = Condition.create("1 < 1");
        assertThat(cond.evaluate(state), equalTo(false));

        Condition cond2 = Condition.create("6748 > 45");
        assertThat(cond2.evaluate(state), equalTo(true));
    }

    @Test
    public void Condition_LessThanEqualTo_GreaterThanEqualTo(){
        Condition cond = Condition.create("50 <= 50");
        assertThat(cond.evaluate(state), equalTo(true));

        Condition cond2 = Condition.create("99 >= 98");
        assertThat(cond2.evaluate(state), equalTo(true));
    }
}
