import java.util.HashMap;
import java.util.Map;

public class InitialValue {

    public static final String NEW_LINE;
    public static final Map<String, String> VALUE_LUT = new HashMap<>();

    static {
        VALUE_LUT.put("boolean", "false");
        VALUE_LUT.put("Boolean", "false");
        VALUE_LUT.put("int", "0");
        VALUE_LUT.put("byte", "(byte)0");
        VALUE_LUT.put("Byte", "(byte)0");
        VALUE_LUT.put("Integer", "0");
        VALUE_LUT.put("String", "\"AnyString\"");
        VALUE_LUT.put("BigDecimal", "new BigDecimal(\"0\")");
        VALUE_LUT.put("Long", "0L");
        VALUE_LUT.put("long", "0L");
        VALUE_LUT.put("short", "(short)0");
        VALUE_LUT.put("Short", "(short)0");
        VALUE_LUT.put("Date", "new Date()");
        VALUE_LUT.put("float", "0.0F");
        VALUE_LUT.put("Float", "0.0F");
        VALUE_LUT.put("double", "0.0D");
        VALUE_LUT.put("Double", "0.0D");
        VALUE_LUT.put("Character", "\'C\'");
        VALUE_LUT.put("char", "\'C\'");
        VALUE_LUT.put("LocalDateTime", "LocalDateTime.now()");
        VALUE_LUT.put("LocalDate", "LocalDate.now()");
        VALUE_LUT.put("List", "[]");

        NEW_LINE = System.getProperty("line.separator");
    }
}
