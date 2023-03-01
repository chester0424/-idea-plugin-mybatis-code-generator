package zone.pusu.mybatisCodeGenerator.setting;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义列
 */
public class SettingExtendCfgCol {

    private List<SettingExtendCfgColItem> items = new ArrayList<>();

    public SettingExtendCfgCol() {
        this.items = getDefaults();
    }

    public List<SettingExtendCfgColItem> getItems() {
        return items;
    }

    public void setItems(List<SettingExtendCfgColItem> items) {
        this.items = items;
    }

    private List<SettingExtendCfgColItem> getDefaults() {
        List<SettingExtendCfgColItem> settingExtendCfgColItems = new ArrayList<>();

        SettingExtendCfgColItem settingExtendCfgColItem = new SettingExtendCfgColItem();
        settingExtendCfgColItem.setName("DbType");
        settingExtendCfgColItem.setType(SettingExtendCfgColTypeEnum.INPUT.name());
        settingExtendCfgColItems.add(settingExtendCfgColItem);

        return settingExtendCfgColItems;
    }
}
