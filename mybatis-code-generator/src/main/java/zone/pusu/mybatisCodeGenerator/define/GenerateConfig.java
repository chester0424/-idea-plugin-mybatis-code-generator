package zone.pusu.mybatisCodeGenerator.define;

import java.io.Serializable;
import java.util.List;

/**
 * 生成mybatis的 类型 配置信息
 */
public class GenerateConfig implements Serializable {
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 字段信息
     */
    private List<GenerateConfigField> fields;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<GenerateConfigField> getFields() {
        return fields;
    }

    public void setFields(List<GenerateConfigField> fields) {
        this.fields = fields;
    }
}

