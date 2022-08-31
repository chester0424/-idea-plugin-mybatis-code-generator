package zone.pusu.mybatisCodeGenerator.define;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 生成mybatis的 类型 配置信息
 */
public class GenerateMybatisConfigClass implements Serializable {
    /**
     * java 包名
     */
    private String packageName;
    /**
     * java 实体对象类名
     */
    private String className;
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 字段信息
     */
    private List<GenerateMybatisConfigField> fields;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<GenerateMybatisConfigField> getFields() {
        return fields;
    }

    public void setFields(List<GenerateMybatisConfigField> fields) {
        this.fields = fields;
    }
}

