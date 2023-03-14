package zone.pusu.mybatisCodeGenerator.setting;

import java.util.*;

/**
 * 类型映射
 */
public class SettingTypeMapping {

    private List<SettingTypeMappingItem> items = new ArrayList<>();

    public SettingTypeMapping() {
        items = getDefault();
    }

    public List<SettingTypeMappingItem> getItems() {
        return items;
    }

    public void setItems(List<SettingTypeMappingItem> items) {
        this.items = items;
    }

    public List<SettingTypeMappingItem> getDefault() {
        Map<String, String> map = new LinkedHashMap<>();
        // 基本类型：数字、布尔、字符
        map.put("byte", "TINYINT");
        map.put("short", "SMALLINT");
        map.put("int", "INTEGER");
        map.put("long", "BIGINT");
        map.put("float", "REAL");
        map.put("double", "DOUBLE");
        map.put("boolean", "BIT");
        map.put("char", "CHAR");
        // 基本类型对应包装类型
        map.put("java.lang.Byte", "TINYINT");
        map.put("java.lang.Short", "SMALLINT");
        map.put("java.lang.Integer", "INTEGER");
        map.put("java.lang.Long", "BIGINT");
        map.put("java.lang.Float", "REAL");
        map.put("java.lang.Double", "DOUBLE");
        map.put("java.lang.Boolean", "BIT");
        map.put("java.lang.Character", "CHAR");
        // 其他常见类型
        map.put("java.lang.String", "VARCHAR");
        map.put("java.lang.Byte[]", "BLOB");
        map.put("byte[]", "BLOB");
        map.put("java.math.BigInteger", "BIGINT");
        map.put("java.math.BigDecimal", "DECIMAL");
        map.put("java.util.Date", "DATETIME"); // date and time
        map.put("java.sql.Date", "DATE"); // only date part
        map.put("java.sql.Time", "TIME"); // only time part
        map.put("java.sql.Timestamp", "TIMESTAMP");  // UTC date and time

        List<SettingTypeMappingItem> items1 = new LinkedList<>();
        for (String key : map.keySet()) {
            SettingTypeMappingItem item = new SettingTypeMappingItem();
            item.setJavaType(key);
            item.setJdbcType(map.get(key));
            items1.add(item);
        }
        return items1;
    }
}
