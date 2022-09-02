package zone.pusu.mybatisCodeGenerator.setting;

/**
 * 类型映射项
 */
public class SettingTypeMappingItem {
    private String javaType;
    private String jdbcType;

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
}
