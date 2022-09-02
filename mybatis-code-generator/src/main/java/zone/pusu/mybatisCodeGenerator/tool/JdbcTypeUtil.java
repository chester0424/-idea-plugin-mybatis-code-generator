package zone.pusu.mybatisCodeGenerator.tool;

import java.sql.JDBCType;
import java.util.Arrays;

/**
 * jdbc 类型
 */
public class JdbcTypeUtil {
    /**
     * 获取所有jdbc类型
     *
     * @return
     */
    public static String[] getAllJdbcTypes() {
        return Arrays.stream(JDBCType.class.getEnumConstants()).map(i -> i.getName()).toArray(String[]::new);
    }
}
