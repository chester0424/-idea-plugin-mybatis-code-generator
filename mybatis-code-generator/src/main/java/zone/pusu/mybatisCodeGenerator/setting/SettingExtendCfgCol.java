package zone.pusu.mybatisCodeGenerator.setting;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义列
 */
public class SettingExtendCfgCol {
    private List<SettingExtendCfgColItem> items = new ArrayList<>();

    public List<SettingExtendCfgColItem> getItems() {
        return items;
    }

    public void setItems(List<SettingExtendCfgColItem> items) {
        this.items = items;
    }
}
