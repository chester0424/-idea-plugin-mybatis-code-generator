package zone.pusu.mybatisCodeGenerator.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * config extend column
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtendColumn {
    /**
     * 自定义列名称
     */
    @XmlAttribute(name = "name")
    private String name;
    /**
     * 自定义列类型 (ExtendColumnTypeEnum)
     * boolean,select,input
     */
    @XmlAttribute(name = "type")
    private String type;
    /**
     * 自定义列选项值
     * 自定义类型为 select, 选择项 各个值用逗号隔开
     */
    @XmlAttribute(name = "options")
    private String options;


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

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
