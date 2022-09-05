package zone.pusu.mybatisCodeGenerator.define;

import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;

import java.util.Map;

public class TemplateDataContextField {
    String name;
    String javaType;
    String columnName;
    String jdbcType;
    boolean primaryKey;
    String typeHandler;
    Map<String, Object> extend;

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getJavaType() {
        return javaType;
    }

    private void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getColumnName() {
        return columnName;
    }

    private void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    private void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    private void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTypeHandler() {
        return typeHandler;
    }

    private void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    private void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
