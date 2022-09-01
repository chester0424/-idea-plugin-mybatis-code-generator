package zone.pusu.mybatisCodeGenerator.tool;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ObjectUtil {
    public static <T> T clone(T source, Type type) {
        return JsonUtil.fromJson(JsonUtil.toJson(source), type);
    }

    public static <T> T clone(T source, TypeToken typeToken) {
        return clone(source, typeToken.getType());
    }
}
