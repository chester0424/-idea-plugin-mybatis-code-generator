package zone.pusu.mybatisCodeGenerator.tool;

public class StringUtil {
    /**
     * 判断是否为空
     *
     * @param val
     * @return
     */
    public static boolean isNullOrEmpty(String val) {
        return val == null || val.trim().equals("");
    }
}
