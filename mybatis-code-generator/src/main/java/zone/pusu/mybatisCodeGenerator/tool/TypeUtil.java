package zone.pusu.mybatisCodeGenerator.tool;

import java.util.LinkedHashMap;
import java.util.Map;

public class TypeUtil {
    public static final Map<String, String> primitiveTypeAndWrappedTypes = new LinkedHashMap<>();

    static {
        primitiveTypeAndWrappedTypes.put("byte", "java.lang.Byte");
        primitiveTypeAndWrappedTypes.put("short", "java.lang.Short");
        primitiveTypeAndWrappedTypes.put("int", "java.lang.Integer");
        primitiveTypeAndWrappedTypes.put("long", "java.lang.Long");
        primitiveTypeAndWrappedTypes.put("float", "java.lang.Float");
        primitiveTypeAndWrappedTypes.put("double", "java.lang.Double");
        primitiveTypeAndWrappedTypes.put("boolean", "java.lang.Boolean");
        primitiveTypeAndWrappedTypes.put("char", "java.lang.Character");
    }

    /**
     * 基本类型转包装类型
     * （java泛型中不支持基本类型）
     *
     * @return
     */
    public static String primitiveTypeToWrappedType(String type) {
        if (!StringUtil.isNullOrEmpty(type)) {
            if (primitiveTypeAndWrappedTypes.containsKey(type)) {
                return primitiveTypeAndWrappedTypes.get(type);
            }
        }
        return type;
    }
}
