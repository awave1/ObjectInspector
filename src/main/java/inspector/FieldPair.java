package inspector;

import java.lang.reflect.Field;

public class FieldPair {
    public Field field;
    public Object value;

    public FieldPair(Field field, Object value) {
         this.field = field;
         this.value = value;
    }
}
