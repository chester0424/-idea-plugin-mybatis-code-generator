package zone.pusu.mybatisCodeGenerator.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * java type to jdbc type mapping
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TypeMapping {

    @XmlAttribute(name = "java")
    private String javaType;

    @XmlAttribute(name = "jdbc")
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
