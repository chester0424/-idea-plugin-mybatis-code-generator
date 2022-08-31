package zone.pusu.mybatisCodeGenerator.define;

import java.lang.reflect.Type;

/**
 * 字段信息
 */
public class FieldInfo {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
