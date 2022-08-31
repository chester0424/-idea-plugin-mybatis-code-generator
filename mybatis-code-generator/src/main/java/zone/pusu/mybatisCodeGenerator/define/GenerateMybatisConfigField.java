package zone.pusu.mybatisCodeGenerator.define;

/**
 * 字段配置信息
 */
public class GenerateMybatisConfigField {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段java类型
     */
    private String javaType;
    /**
     * 字段 对应 jdbc 类型
     */
    private String jdbcType;
    /**
     * 是否是主键
     */
    private boolean primaryKey = false;
    /**
     * 是否生成查询语句
     */
    private boolean query = false;
    /**
     * 是否生成排序语句
     */
    private boolean orderBy = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isQuery() {
        return query;
    }

    public void setQuery(boolean query) {
        this.query = query;
    }

    public boolean isOrderBy() {
        return orderBy;
    }

    public void setOrderBy(boolean orderBy) {
        this.orderBy = orderBy;
    }
}
