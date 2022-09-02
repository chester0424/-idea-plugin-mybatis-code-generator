package zone.pusu.mybatisCodeGenerator.tool;

import java.util.ArrayList;
import java.util.List;

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
        if (source.length() == 1) {
            return source.toLowerCase();
        } else {
            List<String> splits = new ArrayList<>();
            int startIndex = 0;
            for (int i = 1; i < source.length(); i++) {
                boolean isLowerPre = Character.isLowerCase(source.charAt(i - 1));
                boolean isLower = Character.isLowerCase(source.charAt(i));
                if (isLowerPre != isLower) {
                    if (isLowerPre) { // 小->大
                        splits.add(source.substring(startIndex, i - 1));
                        startIndex = i;
                    } else { // 大->小
                        splits.add(source.substring(startIndex, i - 2));
                        startIndex = i - 1;
                    }
                }
            }
            splits.add(source.substring(startIndex, source.length() - 1));

            return String.join("_", splits).toLowerCase();
        }
    }
}
