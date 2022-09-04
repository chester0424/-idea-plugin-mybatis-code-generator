package zone.pusu.mybatisCodeGenerator.setting;

public class SettingExtendCfgColItem {
    /**
     * 自定义列名称
     */
    private String name;
    /**
     * 自定义列类型 (SettingCustomizeCfgColTypeEnum)
     * boolean,select,input
     */
    private String Type;
    /**
     * 自定义列选项值
     * 自定义类型为 select, 选择项 各个值用逗号隔开
     */
    private String options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
