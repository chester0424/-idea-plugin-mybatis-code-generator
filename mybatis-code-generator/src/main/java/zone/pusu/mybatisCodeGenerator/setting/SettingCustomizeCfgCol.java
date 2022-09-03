package zone.pusu.mybatisCodeGenerator.setting;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义列
 */
public class SettingCustomizeCfgCol {
    private List<SettingCustomizeCfgColItem> items = new ArrayList<>();

    public List<SettingCustomizeCfgColItem> getItems() {
        return items;
    }

    public void setItems(List<SettingCustomizeCfgColItem> items) {
        this.items = items;
    }
}
