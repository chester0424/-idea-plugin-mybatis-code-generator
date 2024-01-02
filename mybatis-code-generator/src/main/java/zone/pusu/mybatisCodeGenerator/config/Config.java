package zone.pusu.mybatisCodeGenerator.config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {
    @XmlElement(name = "config-file-save-path")
    private String configFileSavePath = "./dao/";
    @XmlElementWrapper(name = "type-mappings")
    @XmlElement(name = "mapping")
    private List<TypeMapping> typeMappings = new ArrayList<>();
    @XmlElementWrapper(name = "extend-columns")
    @XmlElement(name = "column")
    private List<ExtendColumn> extendColumns = new ArrayList<>();
    @XmlTransient
    private Map<String, String> templates = new HashMap<>();

    public String getConfigFileSavePath() {
        return configFileSavePath;
    }

    public void setConfigFileSavePath(String configFileSavePath) {
        this.configFileSavePath = configFileSavePath;
    }

    public List<TypeMapping> getTypeMappings() {
        return typeMappings;
    }

    public void setTypeMappings(List<TypeMapping> typeMappings) {
        this.typeMappings = typeMappings;
    }

    public List<ExtendColumn> getExtendColumns() {
        return extendColumns;
    }

    public void setExtendColumns(List<ExtendColumn> extendColumns) {
        this.extendColumns = extendColumns;
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }
}
