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

    /**
     * 字符串单词拆分
     *
     * @param source
     * @return
     */
    public static String spiteWord(String source) {
        if (StringUtil.isNullOrEmpty(source)) {
            return source;
        }
        String tempName = source.replaceAll("[A-Z]", "_$0");
        String[] partName = tempName.split("_");
        return String.join("_", partName).toLowerCase();
    }

    /**
     * 首字母消息
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (isNullOrEmpty(s)) {
            return s;
        }
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
