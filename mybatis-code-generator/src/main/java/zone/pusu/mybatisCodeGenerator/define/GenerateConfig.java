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
     * 注释
     */
    private String comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<GenerateConfigField> getFields() {
        return fields;
    }

    public void setFields(List<GenerateConfigField> fields) {
        this.fields = fields;
    }
}

