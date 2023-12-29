package zone.pusu.mybatisCodeGenerator.define;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 字段配置信息
 */
public class GenerateConfigField {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段java类型
     */
    private String javaType;
    /**
     * 注释
     */
    private String comment;
    /**
     * 是否忽略
     */
    private boolean ignore = false;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 字段 对应 jdbc 类型
     */
    private String jdbcType = "";
    /**
     * 是否是主键
     */
    private boolean primaryKey = false;

    /**
     * 类型处理器
     */
    private String typeHandler = "";

    /**
     * 扩展列
     * 存储内容：标识名称，-值
     */
    private Map<String, Object> extend = new LinkedHashMap<>();

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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

    public String getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }
}
