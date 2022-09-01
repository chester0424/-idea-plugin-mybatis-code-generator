package zone.pusu.mybatisCodeGenerator.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * JSON操作
 */
public class JsonUtil {
    /**
     * 对象转json
     *
     * @param source
     * @return
     */
    public static String toJson(Object source) {
        return new Gson().toJson(source);
    }

    /**
     * 对象转json 并对json 格式化
     *
     * @param source
     * @return
     */
    public static String toJsonPretty(Object source) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()   //在序列化的时候不忽略null值
                .create();
        return gson.toJson(source);
    }

    /**
     * json字符串 转对象
     *
     * @param source
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String source, Class<T> clz) {
        return new Gson().fromJson(source, clz);
    }

    public static <T> T fromJson(String source, Type typeOfT) {
        return new Gson().fromJson(source, typeOfT);
    }

    public static <T> T fromJson(String source, TypeToken typeToken) {
        return new Gson().fromJson(source, typeToken.getType());
    }
}
