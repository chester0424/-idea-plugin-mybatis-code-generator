package zone.pusu.mybatisCodeGenerator.setting;

import java.util.*;

/**
 * 类型映射
 */
public class SettingTypeMapping {

    private List<SettingTypeMappingItem> items = new ArrayList<>();

    public SettingTypeMapping() {
        setDefault();
    }

    public List<SettingTypeMappingItem> getItems() {
        return items;
    }

    public void setItems(List<SettingTypeMappingItem> items) {
        this.items = items;
    }

    public void setDefault() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("java.lang.String", "VARCHAR");
        map.put("java.lang.Byte[]", "BLOB");
        map.put("java.lang.Long", "INTEGER");
        map.put("java.lang.Integer", "TINYINT");
        map.put("java.lang.Boolean", "BIT");
        map.put("java.math.BigInteger", "BIGINT");
        map.put("java.lang.Float", "FLOAT");
        map.put("java.lang.Double", "DOUBLE");
        map.put("java.math.BigDecimal", "DECIMAL");
        map.put("java.sql.Date", "DATE");
        map.put("java.sql.Time", "TIME");
        map.put("java.sql.Timestamp", "TIMESTAMP");

        items.clear();
        for (String key : map.keySet()) {
            SettingTypeMappingItem item = new SettingTypeMappingItem();
            item.setJavaType(key);
            item.setJdbcType(map.get(key));
            items.add(item);
        }
    }
}
